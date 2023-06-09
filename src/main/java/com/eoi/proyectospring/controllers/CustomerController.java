package com.eoi.proyectospring.controllers;

import com.eoi.proyectospring.entities.Customer;
import com.eoi.proyectospring.repositories.CustomerRepository;
import com.eoi.proyectospring.services.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 * The type Customer controller.
 */
@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    /**
     * Show sign up form string.
     *
     * @param customer the customer
     * @return the string
     */
    @GetMapping("/new")
    public String showSignUpForm(Customer customer) {
        return "add-customer";
    }

    /**
     * Show user list string.
     *
     * @param model the model
     * @return the string
     */
    @GetMapping("/index")
    public String showUserList(Model model) {
        model.addAttribute("customers", customerService.findAll());
        return "index";
    }

    /**
     * Show update form string.
     *
     * @param id    the id
     * @param model the model
     * @return the string
     */
    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id,Model  model) {
        Customer customer = null;
        try {
            customer = (Customer) customerService.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid customer Id:" + id));

        } catch (Throwable e) {
            log.error("User Not Found");
            return "index";
        }
        model.addAttribute("customer", customer);
        return "update-customer";
    }

    /**
     * Update customer string.
     *
     * @param id       the id
     * @param customer the customer
     * @param result   the result
     * @param model    the model
     * @return the string
     */
    @PostMapping("/update/{id}")
    public String updateCustomer(@PathVariable("id") Integer id,
                                 @Valid Customer customer,
                                 BindingResult result, Model model) {
        if (result.hasErrors()) {
            customer.setId(id);
            return "update-customer";
        }
        customerService.save(customer);
        return "redirect:/customer/index";
    }

    /**
     * Delete customer string.
     *
     * @param id    the id
     * @param model the model
     * @return the string
     */
    @GetMapping("/delete/{id}")
    public String deleteCustomer(@PathVariable("id") Integer id, Model model) {
        try {
            Customer customer = (Customer) customerService.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid customer Id:" + id));

        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        //  customerService.delete(customer);
        return "redirect:/customer/index";
    }

    @PostMapping("/add")
    public String updateCustomer(
                                 @Valid Customer customer,
                                 BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "add-customer";
        }
        customerService.save(customer);
        return "redirect:/customer/index";
    }

}
