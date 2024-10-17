package com.example.cargos_sem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.List;

@Controller
public class AppController {

    @Autowired
    private CargoService service;

    @RequestMapping("/")
    public String viewHomePage(Model model, @Param("keyword") String keyword) {
        List<Cargos> listCargos = service.listAll(keyword);
        model.addAttribute("listCargos", listCargos);
        model.addAttribute("keyword", keyword);
        return "index";
    }

    @RequestMapping("/filterBySendingDate")
    public String filterBySendingDate(Model model, @Param("sending_date") java.sql.Date sending_date) {
        List<Cargos> listCargos = service.filterBySendingDate(sending_date);
        model.addAttribute("listCargos", listCargos);
        model.addAttribute("sending_date", sending_date);
        return "index";
    }

    @RequestMapping("/filterByArrivalDate")
    public String filterByArrivalDate(Model model, @Param("arrival_date") java.sql.Date arrival_date) {
        List<Cargos> listCargos = service.filterByArrivalDate(arrival_date);
        model.addAttribute("listCargos", listCargos);
        model.addAttribute("arrival_date", arrival_date);
        return "index";
    }

    @RequestMapping("/new")
    public String showNewCargoForm(Model model) {
        Cargos cargo = new Cargos();
        model.addAttribute("cargo", cargo);
        return "new_cargo";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String saveCargo(@ModelAttribute("cargo") Cargos cargo) {
        service.save(cargo);
        return "redirect:/";
    }

    @RequestMapping("/edit/{id}")
    public ModelAndView showEditCargoForm(@PathVariable(name = "id") Long id) {
        ModelAndView mav = new ModelAndView("edit_cargo");
        Cargos cargo = service.get(id);
        mav.addObject("cargo", cargo);
        return mav;
    }

    @RequestMapping("/delete/{id}")
    public String deleteCargo(@PathVariable(name = "id") Long id) {
        service.delete(id);
        return "redirect:/";
    }

    @RequestMapping("/chart")
    public String getChart(Model model) {
        String chart = null;
        try {
            chart = service.generateBarChart();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        model.addAttribute("chart", chart);

        return "chart";
    }
}


