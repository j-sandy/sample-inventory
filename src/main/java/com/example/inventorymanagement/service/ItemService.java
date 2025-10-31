package com.example.inventorymanagement.service;

import com.example.inventorymanagement.model.Item;
import com.example.inventorymanagement.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    public Optional<Item> getItemById(Long id) {
        return itemRepository.findById(id);
    }

    public Item saveItem(Item item) {
        return itemRepository.save(item);
    }

    public void deleteItem(Long id) {
        itemRepository.deleteById(id);
    }

    public Optional<Item> getItemByItemCode(String itemCode) {
        return itemRepository.findByItemCode(itemCode);
    }

    public List<Item> getItemsByProcurementDate(LocalDate procurementDate) {
        return itemRepository.findByProcurementDate(procurementDate);
    }

    public List<Item> getItemsByExpiryDate(LocalDate expiryDate) {
        return itemRepository.findByExpiryDate(expiryDate);
    }

    public List<Item> getItemsByName(String name) {
        return itemRepository.findByNameContainingIgnoreCase(name);
    }
}
