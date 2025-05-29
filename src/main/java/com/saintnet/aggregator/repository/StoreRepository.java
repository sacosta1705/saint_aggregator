package com.saintnet.aggregator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

import com.saintnet.aggregator.entity.Store;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long>{
    
    Optional<Store> findByName(String name);
    Optional<Store> findByApiUrl(String apiurl);
    List<Store> findBySyncTru();
}
