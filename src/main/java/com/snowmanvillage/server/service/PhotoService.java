package com.snowmanvillage.server.service;

import com.google.maps.model.LatLng;
import com.snowmanvillage.server.dto.req.LocationRequestDto;
import com.snowmanvillage.server.dto.resp.PhotoResponseDto;
import com.snowmanvillage.server.dto.req.PhotoUploadRequestDto;
import com.snowmanvillage.server.entity.Location;
import com.snowmanvillage.server.entity.Photo;
import com.snowmanvillage.server.entity.global.BaseTimeEntity;
import com.snowmanvillage.server.repository.LocationRepository;
import com.snowmanvillage.server.repository.PhotoRepository;
import jakarta.transaction.Transactional;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class PhotoService {

    private final PhotoRepository photoRepository;
    private final LocationRepository locationRepository;
    private final PasswordBCryptService passwordBCryptService;
    private final GoogleGeocodingService googleGeocodingService;

    @Autowired
    private S3Uploader s3Uploader;

    public Photo uploadPhoto(MultipartFile image, PhotoUploadRequestDto requestDto,
                             LocationRequestDto locationRequestDto) {
        String photoUrl = s3Uploader.upload(image, "images");
        Photo savedPhoto = photoRepository.save(requestDto.toEntity(photoUrl, requestDto));
        locationRepository.save(locationRequestDto.toEntity(locationRequestDto, savedPhoto));
        return savedPhoto;
    }

    public PhotoResponseDto getPhotoByPhotoId(Long id) {
        Photo photo = photoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 포토가 없습니다."));
        return PhotoResponseDto.of(photo);
    }

    public List<PhotoResponseDto> getPhotoList(String orderBy) {
        // orderBy : LIKES, DATE(최신순)
        List<Photo> photoList = photoRepository.findAll();
        switch (orderBy) {
            case "LIKES":
                photoList.sort((o1, o2) -> o2.getLikeCount().compareTo(o1.getLikeCount()));
                break;
            case "DATE":
                photoList.sort(Comparator.comparing(BaseTimeEntity::getCreatedDate));
                break;
            default:
                throw new IllegalArgumentException("orderBy 값이 잘못되었습니다.");
        }
        return PhotoResponseDto.listOf(photoList);
    }

    @Transactional
    public String deletePhoto(Long photoId, String password) {
        Photo photo = photoRepository.findById(photoId).orElseThrow(() -> new IllegalArgumentException("해당 포토가 없습니다."));
        locationRepository.findByPhoto(photo).ifPresent(locationRepository::delete);
        if (passwordBCryptService.matchPassword(password, photo.getPassword())) {
            photoRepository.delete(photo);
            return "포토 삭제 완료";
        } else {
            return "비밀번호가 틀렸습니다.";
        }
    }

    @Transactional
    public PhotoResponseDto likePhoto(Long photoId) {
        Photo photo = photoRepository.findById(photoId).orElseThrow(() -> new IllegalArgumentException("해당 포토가 없습니다."));
        photo.setLikeCount(photo.getLikeCount() + 1);
        return PhotoResponseDto.of(photo);
    }

    @Transactional
    public PhotoResponseDto unlikePhoto(Long photoId) {
        Photo photo = photoRepository.findById(photoId).orElseThrow(() -> new IllegalArgumentException("해당 포토가 없습니다."));
        photo.setLikeCount(photo.getLikeCount() - 1);
        return PhotoResponseDto.of(photo);
    }

    public List<PhotoResponseDto> searchPhoto(String searchType, String searchValue) {
        // searchType : TITLE, WRITER, LOCATION
        List<Photo> photoList = switch (searchType) {
            case "TITLE" -> photoRepository.findByTitleContaining(searchValue);
            case "WRITER" -> photoRepository.findByUsernameContaining(searchValue);
            case "LOCATION" -> searchPhotoByLocation(searchValue);
            default -> throw new IllegalArgumentException("searchType 값이 잘못되었습니다.");
        };
        return PhotoResponseDto.listOf(photoList);
    }

    public List<Photo> searchPhotoByLocation(String searchValue) {
        Map<String, LatLng> locationWithBounds =
            googleGeocodingService.getLocationWithBounds(searchValue);
        LatLng northEast = locationWithBounds.get("northEast");
        LatLng southWest = locationWithBounds.get("southWest");

        List<Photo> photoListByLocationContaining = locationRepository.findByLocationContaining(searchValue);
        List<Photo> photoListByGeocoding = locationRepository.findAll()
            .stream()
            .filter(location -> location.getLongitude() != null && location.getLatitude() != null)
            .filter(location -> location.getLatitude() >= southWest.lat && location.getLatitude() <= northEast.lat
                && location.getLongitude() >= southWest.lng && location.getLongitude() <= northEast.lng)
            .map(location -> photoRepository.findById(location.getPhoto().getId())
                .orElseThrow(() -> new IllegalArgumentException("해당 포토가 없습니다.")))
            .toList();

        Set<Photo> photoSet = new HashSet<>(photoListByLocationContaining);
        photoSet.addAll(photoListByGeocoding);
        return photoSet.stream().toList();
    }
}
