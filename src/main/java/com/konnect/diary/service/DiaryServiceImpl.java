package com.konnect.diary.service;

import com.konnect.comment.CommentRepository;
import com.konnect.comment.CommentService;
import com.konnect.comment.dto.CommentDto;
import com.konnect.diary.dto.DiarySortType;
import com.konnect.diary.dto.request.AreaRequestDTO;
import com.konnect.diary.dto.request.CreateDiaryDraftRequestDTO;
import com.konnect.diary.dto.request.DiaryRouteDTO;
import com.konnect.diary.dto.request.DiaryRouteDetailDTO;
import com.konnect.diary.dto.response.CreateDiaryResponseDTO;
import com.konnect.diary.dto.response.DetailDiaryResponseDTO;
import com.konnect.diary.dto.response.ListDiaryResponseDTO;
import com.konnect.diary.entity.DiaryEntity;
import com.konnect.diary.entity.DiaryTagEntity;
import com.konnect.diary.repository.DetailDiaryProjection;
import com.konnect.diary.repository.DiaryRepository;
import com.konnect.diary.repository.DiaryTagRepository;
import com.konnect.diary.repository.ListDiaryProjection;
import com.konnect.diary.service.exception.DiaryRuntimeException;
import com.konnect.repository.AreaRepository;
import com.konnect.route.entity.Route;
import com.konnect.route.repository.RouteRepository;
import com.konnect.tag.TagEntity;
import com.konnect.tag.TagRepository;
import com.konnect.tag.TagResponseDTO;
import com.konnect.user.repository.UserRepository;
import com.konnect.util.DateTimeUtils;
import com.konnect.util.FileStorage;
import com.konnect.util.ImageManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiaryServiceImpl implements DiaryService {

    private final CommentService commentService;

    private final DiaryRepository diaryRepository;
    private final DiaryTagRepository diaryTagRepository;
    private final TagRepository tagRepository;
    private final AreaRepository areaRepository;
    private final UserRepository userRepository;
    private final RouteRepository routeRepository;

    private final ImageManager imageManager;
    private final FileStorage fileStorage;

    @Override
    @Transactional
    public CreateDiaryResponseDTO createDiary(
            CreateDiaryDraftRequestDTO dto,
            Long userId,
            MultipartFile thumbnail,
            List<MultipartFile> imageFiles
    ) {
        byte[] thumbBytes = readBytesOrNull(thumbnail, "thumbnail");
        List<byte[]> imgBytes  = readBytesList(imageFiles, "image");

        DiaryEntity diary = new DiaryEntity();
        applyCommonFields(diary, dto, userId);
        diary.setStatus(dto.getStatus());
        diaryRepository.save(diary);

        syncTags(diary, dto.getTags());
        syncRoutes(diary, dto.getRoutes());
        imageManager.saveAllImages(diary.getDiaryId(), thumbnail, imageFiles);

        return buildResponse(diary, thumbBytes, imgBytes, dto.getRoutes());
    }

    @Override
    @Transactional
    public CreateDiaryResponseDTO editDiary(
            CreateDiaryDraftRequestDTO dto,
            Long userId,
            MultipartFile thumbnail,
            List<MultipartFile> imageFiles
    ) {
        byte[] thumbBytes = readBytesOrNull(thumbnail, "thumbnail");
        List<byte[]> imgBytes  = readBytesList(imageFiles, "image");

        Long id = dto.getDiaryId()
                .orElseThrow(() -> new DiaryRuntimeException("Diary ID is required"));
        DiaryEntity diary = diaryRepository.findById(id)
                .orElseThrow(() -> new DiaryRuntimeException("Draft not found: " + id));
        if ("published".equalsIgnoreCase(diary.getStatus())) {
            throw new DiaryRuntimeException("Cannot modify a published diary: " + id);
        }

        applyCommonFields(diary, dto, userId);
        diary.setStatus("editing");
        diaryRepository.save(diary);

        syncTags(diary, dto.getTags());
        syncRoutes(diary, dto.getRoutes());
        imageManager.saveAllImages(diary.getDiaryId(), thumbnail, imageFiles);

        return buildResponse(diary, thumbBytes, imgBytes, dto.getRoutes());
    }

    @Override
    public List<ListDiaryResponseDTO> fetchDiaries(Long areaId, boolean topOnly, DiarySortType sortedBy) {
        Pageable pageable = createPageable(topOnly, sortedBy);
        Page<ListDiaryProjection> pages = diaryRepository.findDiariesByArea(areaId, pageable);
        return toResponseList(pages);
    }

    @Override
    public List<ListDiaryResponseDTO> fetchMyDiaries(Long userId) {
        Pageable pageable = PageRequest.of(
                0, Integer.MAX_VALUE,
                Sort.by(Sort.Order.asc("status"), Sort.Order.desc("created_at"))
        );
        Page<ListDiaryProjection> pages = diaryRepository.fetchMyDiaries(userId, pageable);
        return toResponseList(pages);
    }

    @Override
    public DetailDiaryResponseDTO fetchDiaryDetail(Long diaryId, Long userId) {
        if (!diaryRepository.existsById(diaryId))
            throw new DiaryRuntimeException("cannot find diary with id: " + diaryId);

        DetailDiaryProjection projection = diaryRepository.fetchDiaryDetail(diaryId, userId);
        List<TagResponseDTO> tags = diaryTagRepository
                .findTop3ByDiary_DiaryIdOrderByIdAsc(diaryId)
                .stream()
                .map(t -> new TagResponseDTO(t.getTagId(), t.getName(), t.getNameEng()))
                .toList();
        List<DiaryRouteDTO> routeDtos = fetchRoutes(diaryId);
        List<CommentDto> comments = commentService.getCommentsByDiary(diaryId);

        return DetailDiaryResponseDTO.from(projection, tags, routeDtos, comments);
    }

    private List<DiaryRouteDTO> fetchRoutes(Long diaryId) {
        List<Route> routeEntities = routeRepository
                .findAllByDiary_DiaryIdOrderByVisitedDateAscVisitedTimeAsc(diaryId);

        Map<String, List<Route>> grouped = routeEntities.stream()
                .collect(Collectors.groupingBy(
                        Route::getVisitedDate,
                        LinkedHashMap::new,
                        Collectors.toList()
                ));

        List<DiaryRouteDTO> dtos = grouped.entrySet().stream()
                .map(entry -> {
                    String date = entry.getKey();
                    List<DiaryRouteDetailDTO> details = entry.getValue().stream()
                            .map(r -> {
                                DiaryRouteDetailDTO detail = DiaryRouteDetailDTO.builder()
                                        .visitedDate(r.getVisitedDate())
                                        .visitedTime(r.getVisitedTime())
                                        .distance(r.getDistance())
                                        .title(r.getTitle())
                                        .latitude(r.getLatitude())
                                        .longitude(r.getLongitude())
                                        .build();
                                return detail;
                            })
                            .toList();

                    return new DiaryRouteDTO(date, details);                        // date + items
                })
                .toList();

        return dtos;
    }

    private Pageable createPageable(boolean topOnly, DiarySortType sortType) {
        Sort sort = switch (sortType) {
            case RECENT     -> Sort.by(Sort.Direction.DESC, "created_at");
            case MOST_LIKED -> Sort.by(Sort.Direction.DESC, "likeCount");
        };
        int size = topOnly ? 4 : Integer.MAX_VALUE;
        return PageRequest.of(0, size, sort);
    }

    private List<ListDiaryResponseDTO> toResponseList(Page<ListDiaryProjection> pages) {
        return pages.getContent().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private ListDiaryResponseDTO toResponse(ListDiaryProjection p) {
        List<TagResponseDTO> tags = diaryTagRepository
                .findTop3ByDiary_DiaryIdOrderByIdAsc(p.getDiaryId())
                .stream()
                .map(t -> new TagResponseDTO(t.getTagId(), t.getName(), t.getNameEng()))
                .collect(Collectors.toList());

        String thumbnail = fileStorage.loadThumbnailBase64(p.getDiaryId());
        AreaRequestDTO area     = new AreaRequestDTO(p.getAreaId(), p.getAreaName());

        return ListDiaryResponseDTO.builder()
                .diaryId(p.getDiaryId())
                .title(p.getTitle())
                .status(p.getStatus())
                .thumbnailImage(thumbnail)
                .area(area)
                .likeCount(p.getLikeCount())
                .startDate(p.getStartDate())
                .endDate(p.getEndDate())
                .tags(tags)
                .build();
    }

    private void applyCommonFields(DiaryEntity diary,
                                   CreateDiaryDraftRequestDTO dto,
                                   Long userId) {
        diary.setUser(userRepository.getReferenceById(userId));
        diary.setTitle(dto.getTitle());
        diary.setContent(dto.getContent().orElse(null));
        diary.setArea(dto.getAreaId()
                .map(areaRepository::getReferenceById)
                .orElse(null));
        diary.setStartDate(dto.getStartDate().orElse(null));
        diary.setEndDate(dto.getEndDate().orElse(null));
        diary.setCreatedAt(DateTimeUtils.getNowDateString());
    }

    private CreateDiaryResponseDTO buildResponse(
            DiaryEntity diary,
            byte[] thumbBytes,
            List<byte[]> imgBytes,
            List<DiaryRouteDTO> routes
    ) {
        String thumbBase64 = thumbBytes == null
                ? null
                : "data:image/*;base64," + Base64.getEncoder().encodeToString(thumbBytes);

        List<String> imgBase64 = imgBytes.stream()
                .map(b -> "data:image/*;base64," + Base64.getEncoder().encodeToString(b))
                .toList();

        return CreateDiaryResponseDTO.from(diary, thumbBase64, imgBase64, routes);
    }

    private void syncTags(DiaryEntity diary, List<Long> tagIds) {
        try {
            diaryTagRepository.deleteByDiary(diary);
            diary.getTags().clear();

            tagIds.stream()
                    .limit(3)
                    .forEach(tagId -> {
                        TagEntity tag = tagRepository.getReferenceById(tagId);
                        DiaryTagEntity dt = new DiaryTagEntity();
                        dt.setDiary(diary);
                        dt.setTag(tag);
                        diaryTagRepository.save(dt);
                        diary.getTags().add(dt);
                    });
        } catch (Exception ex) {
            System.out.println("DiaryServiceImpl: syncTags" + ex.getMessage());
            throw new DiaryRuntimeException("Failed to sync tags: " + ex.getMessage());
        }
    }

    private void syncRoutes(DiaryEntity diary, List<DiaryRouteDTO> dayRoutes) {
        routeRepository.deleteByDiaryDiaryId(diary.getDiaryId());

        List<Route> entities = new ArrayList<>();
        for (DiaryRouteDTO dayDto : dayRoutes) {
            String date = dayDto.getDate();
            for (DiaryRouteDetailDTO item : dayDto.getItems()) {
                Route route = Route.builder()
                        .diary(diary)
                        .visitedDate(date)
                        .visitedTime(item.getVisitedTime())
                        .distance(item.getDistance())
                        .title(item.getTitle())
                        .latitude(item.getLatitude())
                        .longitude(item.getLongitude())
                        .build();
                entities.add(route);
            }
        }

        routeRepository.saveAll(entities);
    }

    private byte[] readBytesOrNull(MultipartFile file, String who) {
        if (file == null || file.isEmpty()) return null;
        try {
            return file.getBytes();
        } catch (IOException e) {
            throw new DiaryRuntimeException("Failed to read " + who + " bytes");
        }
    }

    private List<byte[]> readBytesList(List<MultipartFile> files, String who) {
        if (files == null) return List.of();
        List<byte[]> list = new ArrayList<>();
        for (int i = 0; i < Math.min(files.size(), 9); i++) {
            MultipartFile mf = files.get(i);
            if (mf.isEmpty()) continue;
            try {
                list.add(mf.getBytes());
            } catch (IOException e) {
                throw new DiaryRuntimeException("Failed to read " + who + " bytes");
            }
        }
        return list;
    }
}