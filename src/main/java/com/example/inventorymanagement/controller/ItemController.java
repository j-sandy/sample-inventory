package com.example.inventorymanagement.controller;

import com.example.inventorymanagement.model.Item;
import com.example.inventorymanagement.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<Item> getAllItems() {
        return itemService.getAllItems();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Item> getItemById(@PathVariable Long id) {
        Optional<Item> item = itemService.getItemById(id);
        return item.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Item> createItem(@RequestBody Item item) {
        Item savedItem = itemService.saveItem(item);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedItem);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Item> updateItem(@PathVariable Long id, @RequestBody Item itemDetails) {
        return itemService.getItemById(id)
                .map(item -> {
                    item.setName(itemDetails.getName());
                    item.setItemCode(itemDetails.getItemCode());
                    item.setImage(itemDetails.getImage());
                    item.setDescription(itemDetails.getDescription());
                    item.setQuantity(itemDetails.getQuantity());
                    item.setProcurementDate(itemDetails.getProcurementDate());
                    item.setManufacturingDate(itemDetails.getManufacturingDate());
                    item.setExpiryDate(itemDetails.getExpiryDate());
                    return ResponseEntity.ok(itemService.saveItem(item));
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        if (itemService.getItemById(id).isPresent()) {
            itemService.deleteItem(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<Item> searchItems(
            @RequestParam(required = false) String itemCode,
            @RequestParam(required = false) @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) LocalDate procurementDate,
            @RequestParam(required = false) @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) LocalDate expiryDate,
            @RequestParam(required = false) String name) {

        if (itemCode != null) {
            return itemService.getItemByItemCode(itemCode).map(List::of).orElse(List.of());
        } else if (procurementDate != null) {
            return itemService.getItemsByProcurementDate(procurementDate);
        } else if (expiryDate != null) {
            return itemService.getItemsByExpiryDate(expiryDate);
        } else if (name != null) {
            return itemService.getItemsByName(name);
        }
        return itemService.getAllItems();
    }
}
