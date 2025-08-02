package com.example.service;

import com.example.dto.base.ApiResult;
import com.example.dto.city.*;
import com.example.entity.CityEntity;
import com.example.entity.DistrictEntity;
import com.example.entity.ProfileEntity;
import com.example.enums.LanguageEnum;
import com.example.exp.AppBadException;
import com.example.repository.CityRepository;
import com.example.repository.DistrictRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CityService {

    private final CityRepository cityRepository;
    private final DistrictRepository districtRepository;
    private final ResourceBundleService messageService;


    public ApiResult<CityResponseDTO> createCity(CityCreateDTO dto) {
        CityEntity city = new CityEntity();
        city.setNameUz(dto.getNameUz());
        city.setNameEn(dto.getNameEn());
        city.setNameRu(dto.getNameRu());
        city.setOrderNumber(dto.getOrderNumber());
        city.setCreatedDate(LocalDateTime.now());

        List<DistrictEntity> districts = dto.getDistricts().stream()
                .map(districtDTO -> {
                    DistrictEntity district = new DistrictEntity();
                    district.setNameUz(districtDTO.getNameUz());
                    district.setNameRu(districtDTO.getNameRu());
                    district.setNameEn(districtDTO.getNameEn());
                    district.setCity(city);
                    return district;
                })
                .collect(Collectors.toList());

        city.setDistricts(districts);

        CityEntity savedCity = cityRepository.save(city);

        return ApiResult.successResponse(mapToCityResponseDTO(savedCity));
    }

    public ApiResult<List<CityResponseAllDTO>> getAll() {
        Iterable<CityEntity> entity = cityRepository.findAll();
        List<CityResponseAllDTO> dtoList = new LinkedList<>();
        for (CityEntity city : entity) {
            dtoList.add(mapToCityDTO(city));
        }
        return ApiResult.successResponse(dtoList);
    }

    private CityResponseAllDTO mapToCityDTO(CityEntity city) {
        CityResponseAllDTO response = new CityResponseAllDTO();
        response.setNameUz(city.getNameUz());
        response.setNameEn(city.getNameEn());
        response.setNameRu(city.getNameRu());

        List<DistrictDTO> districtDTOs = city.getDistricts().stream()
                .map(this::mapToDistrictResponseDTO)
                .collect(Collectors.toList());

        response.setDistricts(districtDTOs);
        return response;
    }

    private CityResponseDTO mapToCityResponseDTO(CityEntity city) {
        CityResponseDTO response = new CityResponseDTO();
        response.setId(city.getId());
        response.setNameUz(city.getNameUz());
        response.setNameEn(city.getNameEn());
        response.setNameRu(city.getNameRu());

        List<DistrictDTO> districtDTOs = city.getDistricts().stream()
                .map(this::mapToDistrictResponseDTO)
                .collect(Collectors.toList());

        response.setDistricts(districtDTOs);
        return response;
    }

    private DistrictDTO mapToDistrictResponseDTO(DistrictEntity district) {
        DistrictDTO dto = new DistrictDTO();
        dto.setId(district.getId());
        dto.setNameUz(district.getNameUz());
        dto.setNameRu(district.getNameRu());
        dto.setNameEn(district.getNameEn());
        return dto;
    }

    public ApiResult<SearchResultDTO> citySearch(String query, String language) {
        String normalizedQuery = query.trim().toLowerCase();
        List<CityEntity> allCities = cityRepository.findAll();
        List<DistrictEntity> allDistricts = districtRepository.findAll();

        List<CityResponseDTO> cityDTOs = allCities.stream()
                .map(city -> {
                    boolean cityMatches = matchesLanguage(city, normalizedQuery, language);

                    List<DistrictDTO> matchingDistrictsInCity = allDistricts.stream()
                            .filter(district -> district.getCity() != null && district.getCity().getId().equals(city.getId())) // Faqat shu shaharning tumanlari
                            .filter(district -> matchesLanguage(district, normalizedQuery, language)) // Tuman nomiga mos kelish
                            .sorted(Comparator.comparing(district -> calculateRelevance(district, normalizedQuery, language)))
                            .map(this::convertToDistrictResponseDTO)
                            .collect(Collectors.toList());

                    if (cityMatches || !matchingDistrictsInCity.isEmpty()) {
                        return convertToCityResponseDTO(city, matchingDistrictsInCity);
                    }
                    return null;
                })
                .filter(java.util.Objects::nonNull) // null bo'lgan shaharlarni filtrlash
                .sorted(Comparator.comparing(cityDto -> calculateCityRelevance(cityDto, normalizedQuery, language))) // Shaharlarni ham o'z moslik darajasi bo'yicha saralash
                .collect(Collectors.toList());

        return new ApiResult<>(new SearchResultDTO(cityDTOs), true, messageService.getMessage("search.results.retrieved", language));
    }

    private CityResponseDTO convertToCityResponseDTO(CityEntity city) {
        CityResponseDTO dto = new CityResponseDTO();
        dto.setId(city.getId());
        dto.setNameUz(city.getNameUz());
        dto.setNameRu(city.getNameRu());
        dto.setNameEn(city.getNameEn());
        return dto;
    }

    private DistrictDTO convertToDistrictResponseDTO(DistrictEntity district) {
        if (district == null) return null;

        DistrictDTO dto = new DistrictDTO();
        dto.setId(district.getId());
        dto.setNameUz(district.getNameUz());
        dto.setNameRu(district.getNameRu());
        dto.setNameEn(district.getNameEn());
        return dto;
    }

    // Tilga qarab moslikni tekshirish
    private boolean matchesLanguage(CityEntity city, String query, String language) {
        String nameToCheck = switch (language.toLowerCase()) {
            case "ru" -> city.getNameRu().toLowerCase();
            case "en" -> city.getNameEn().toLowerCase();
            default -> city.getNameUz().toLowerCase();
        };
        return nameToCheck.contains(query);
    }


    private boolean matchesLanguage(DistrictEntity district, String query, String language) {
        String name = switch (language.toLowerCase()) {
            case "ru" -> district.getNameRu().toLowerCase();
            case "en" -> district.getNameEn().toLowerCase();
            default -> district.getNameUz().toLowerCase();
        };
        return name.contains(query);
    }


    private int calculateRelevance(Object entity, String normalizedQuery, String language) {
        String name = "";
        if (entity instanceof CityEntity) {
            CityEntity city = (CityEntity) entity;
            if ("uz".equalsIgnoreCase(language)) {
                name = Optional.ofNullable(city.getNameUz()).orElse("");
            } else if ("ru".equalsIgnoreCase(language)) {
                name = Optional.ofNullable(city.getNameRu()).orElse("");
            } else if ("en".equalsIgnoreCase(language)) {
                name = Optional.ofNullable(city.getNameEn()).orElse("");
            }
        } else if (entity instanceof DistrictEntity) {
            DistrictEntity district = (DistrictEntity) entity;
            if ("uz".equalsIgnoreCase(language)) {
                name = Optional.ofNullable(district.getNameUz()).orElse("");
            } else if ("ru".equalsIgnoreCase(language)) {
                name = Optional.ofNullable(district.getNameRu()).orElse("");
            } else if ("en".equalsIgnoreCase(language)) {
                name = Optional.ofNullable(district.getNameEn()).orElse("");
            }
        }

        if (name.equals(normalizedQuery)) {
            return 0;
        } else if (name.startsWith(normalizedQuery)) {
            return 1;
        } else {
            return 2;
        }
    }
    private int calculateCityRelevance(CityResponseDTO cityDto, String normalizedQuery, String language) {
        int cityRelevance = calculateRelevance(new CityEntity(cityDto.getId(), cityDto.getNameUz(), cityDto.getNameRu(), cityDto.getNameEn()), normalizedQuery, language);

        // Agar shaharning o'z nomi mos kelmasa, uning tumanlarining mosligini tekshiramiz
        if (cityRelevance > 0 && !cityDto.getDistricts().isEmpty()) {
            // Tumanlar orasidagi eng yaxshi moslikni topamiz
            int minDistrictRelevance = cityDto.getDistricts().stream()
                    .mapToInt(districtDto -> calculateRelevance(new DistrictEntity(districtDto.getId(), districtDto.getNameUz(), districtDto.getNameRu(), districtDto.getNameEn(), new CityEntity(districtDto.getId(), null, null, null)), normalizedQuery, language))
                    .min()
                    .orElse(Integer.MAX_VALUE);

            return Math.min(cityRelevance, minDistrictRelevance);
        }
        return cityRelevance;
    }

    private CityResponseDTO convertToCityResponseDTO(CityEntity city, List<DistrictDTO> districts) {
        return new CityResponseDTO(city.getId(), city.getNameUz(), city.getNameRu(), city.getNameEn(), districts);
    }

    public CityEntity getById(Long id, LanguageEnum language) {
        return cityRepository.findById(id).orElseThrow(() -> new AppBadException(messageService.getMessage("city.not.found", language)));
    }
}

