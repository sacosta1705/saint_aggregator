package com.saintnet.aggregator.service;

import com.saintnet.aggregator.dto.StoreDto;
import com.saintnet.aggregator.entity.Store;
import com.saintnet.aggregator.repository.StoreRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StoreService {
    private final StoreRepository storeRepository;
    private final PasswordEncoder passwordEncoder;

    // @Autowired
    public StoreService(StoreRepository sr, PasswordEncoder pe){
        this.storeRepository = sr;
        this.passwordEncoder = pe;
    }

    @Transactional
    public StoreDto createStore(StoreDto storeDto){
        if(storeRepository.findByName(storeDto.getName()).isPresent()){
            throw new IllegalArgumentException("Ya existe una conexion con el nombre suministrado: " + storeDto.getName());
        }

        if(storeRepository.findByApiUrl(storeDto.getApiurl()).isPresent()){
            throw new IllegalArgumentException("Ya existe una conexion con el URL suministrado: " + storeDto.getApiurl());
        }

        Store store = new Store();
        store.setName(storeDto.getName());
        store.setApiurl(storeDto.getApiurl());
        store.setApiuser(storeDto.getApiuser());
        store.setApipwdhash(passwordEncoder.encode(storeDto.getApipwd()));
        store.setCansync(storeDto.getCansync() != null ? storeDto.getCansync() : true);

        Store savedStore = storeRepository.save(store);
        return mapToDto(savedStore);
    }

    @Transactional(readOnly = true)
    public List<StoreDto> getAllStores (){
        return storeRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<StoreDto> getStoreById(Long id){
        return storeRepository.findById(id).map(this::mapToDto);
    }

    @Transactional(readOnly = true)
    public Optional<Store> findStoreEntityById(Long id){
        return storeRepository.findById(id);
    }

    @Transactional
    public StoreDto updateStore(Long id, StoreDto storeDto){
        Store store = storeRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Tienda no encontrada con ID suministrado: " + id));

        if(store.getName().equals(storeDto.getName()) && storeRepository.findByName(storeDto.getName()).filter(s -> !s.getId().equals(id)).isPresent()){
            throw new IllegalArgumentException("Ya existe una tienda con el nombre suministrado.");
        }
        if (!store.getApiurl().equals(storeDto.getApiurl()) && storeRepository.findByApiUrl(storeDto.getApiurl()).filter(s -> !s.getId().equals(id)).isPresent()) {
            throw new IllegalArgumentException("Ya existe otra tienda con la URL de API: " + storeDto.getApiurl());
        }

        store.setName(storeDto.getName());
        store.setApipwdhash(storeDto.getApiurl());
        store.setApiuser(storeDto.getApiuser());

        if (storeDto.getApipwd() != null && !storeDto.getApipwd().isEmpty()) {
            store.setApipwdhash(passwordEncoder.encode(storeDto.getApipwd()));
        }

        store.setCansync(storeDto.getCansync() != null ? storeDto.getCansync() : storeDto.getCansync());

        Store updatedStore = storeRepository.save(store);
        return mapToDto(updatedStore);
    }

    @Transactional
    public void deleteStore(Long id){
        if(!storeRepository.existsById(id)){
            throw new IllegalArgumentException("Conexion con ID: " + id + " no encontrada");
        }
        storeRepository.deleteById(id);
    }

    @Transactional
    public StoreDto toggleSyncStatus(Long id){
        Store store = storeRepository.findById(id)
        .orElseThrow(()->new IllegalArgumentException("Conexion no encontrada."));

        store.setCansync(!store.getCansync());
        Store updatedStore = storeRepository.save(store);
        return mapToDto(updatedStore); 
    }

    @Transactional(readOnly = true)
    public List<Store> getAllStoresActiveForSyncing(){
        return storeRepository.findBySyncTru();
    }

    private StoreDto mapToDto(Store store){
        StoreDto dto = new StoreDto();
        dto.setId(store.getId());
        dto.setName(store.getName());
        dto.setApiurl(store.getApiurl());
        dto.setApiuser(store.getApiuser());
        dto.setCansync(store.getCansync());
        return dto;
    }
}
