package com.saintnet.aggregator.controller;

import com.saintnet.aggregator.dto.StoreDto;
import com.saintnet.aggregator.service.StoreService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin-tiendas") // Todas las rutas de este controlador empezarán con /admin-tiendas
public class AdminStoreController {

    private final StoreService storeService;

    public AdminStoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    // Muestra la lista de tiendas y el formulario para agregar una nueva
    @GetMapping
    public String showStoresPage(Model model) {
        List<StoreDto> stores = storeService.getAllStores();
        model.addAttribute("stores", stores);
        // Si no hay un objeto 'newStore' ya en el modelo (por un error de validación previo),
        // se crea uno nuevo.
        if (!model.containsAttribute("newStore")) {
            model.addAttribute("newStore", new StoreDto());
        }
        return "admin/manage-stores"; // Nombre del archivo HTML de Thymeleaf (ej. manage-stores.html)
    }

    // Procesa el envío del formulario para agregar una nueva tienda
    @PostMapping("/add")
    public String addStore(@Valid @ModelAttribute("newStore") StoreDto storeDto,
    BindingResult result, RedirectAttributes redirectAttributes, Model model) { // Añadido Model aquí
        if (result.hasErrors()) {
            // Si hay errores, volvemos a mostrar la página principal,
            // pero esta vez Thymeleaf usará el objeto 'newStore' con los errores.
            // También necesitamos recargar la lista de tiendas.
            model.addAttribute("stores", storeService.getAllStores());
            return "admin/manage-stores";
        }
        try {
            storeService.createStore(storeDto);
            redirectAttributes.addFlashAttribute("successMessage", "¡Tienda agregada exitosamente!");
        } catch (IllegalArgumentException e) {
            // Si el servicio lanza una excepción (ej. nombre o URL duplicada)
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin-tiendas"; // Redirige de vuelta a la página principal de admin
    }

    // Muestra el formulario para editar una tienda existente
    @GetMapping("/edit/{id}")
    public String showEditStoreForm(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<StoreDto> storeDtoOptional = storeService.getStoreById(id);
        if (storeDtoOptional.isPresent()) {
            StoreDto storeDto = storeDtoOptional.get();
            // Importante: No pasar la contraseña (ni su hash) al formulario de edición directamente
            // a menos que se quiera cambiar. El DTO StoreDto tiene 'apiPassword' para la entrada.
            storeDto.setApipwd(null); // Limpiar campo de contraseña para el formulario
            model.addAttribute("storeToEdit", storeDto);
            return "admin/edit-store"; // Plantilla edit-store.html
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Tienda no encontrada con ID: " + id);
            return "redirect:/admin-tiendas";
        }
    }

    // Procesa el envío del formulario para actualizar una tienda
    @PostMapping("/update/{id}")
    public String updateStore(@PathVariable("id") Long id, @Valid @ModelAttribute("storeToEdit") StoreDto storeDto, BindingResult result, RedirectAttributes redirectAttributes, Model model) { // Añadido Model
        if (result.hasErrors()) {
            storeDto.setId(id); // Es importante re-establecer el ID en el DTO para el formulario de edición
            // Los errores se mostrarán en la plantilla 'edit-store'
            return "admin/edit-store";
        }
        try {
            storeService.updateStore(id, storeDto);
            redirectAttributes.addFlashAttribute("successMessage", "¡Tienda actualizada exitosamente!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            // Si hay un error de lógica de negocio (ej. URL duplicada), redirigir de vuelta al formulario de edición
            // Es importante re-establecer el ID en el DTO para el formulario de edición
            storeDto.setId(id);
            model.addAttribute("storeToEdit", storeDto); // Añadir el objeto con errores al modelo
            return "admin/edit-store"; // Volver a la página de edición
        }
        return "redirect:/admin-tiendas";
    }

    // Procesa la eliminación de una tienda
    @GetMapping("/delete/{id}") // Usar POST o DELETE para acciones destructivas es mejor, pero GET es más simple para un enlace
    public String deleteStore(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            storeService.deleteStore(id);
            redirectAttributes.addFlashAttribute("successMessage", "¡Tienda eliminada exitosamente!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin-tiendas";
    }

    // Procesa el cambio de estado de sincronización de una tienda
    @PostMapping("/toggle-sync/{id}")
    public String toggleSyncStatus(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            storeService.toggleSyncStatus(id);
            redirectAttributes.addFlashAttribute("successMessage", "¡Estado de sincronización de la tienda actualizado!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin-tiendas";
    }
}