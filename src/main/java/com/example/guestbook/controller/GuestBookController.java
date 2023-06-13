package com.example.guestbook.controller;

import com.example.guestbook.dto.GuestbookDTO;
import com.example.guestbook.dto.PageRequestDTO;
import com.example.guestbook.service.GuestbookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/guestbook")
@Log4j2
@RequiredArgsConstructor
public class GuestBookController {
    private final GuestbookService service;
    @GetMapping("/")
    public String index(){
        return "redirect:/guestbook/list";
    }
    @GetMapping("/list")
    public void list(PageRequestDTO pageRequestDTO, Model model){
        log.info("페이지 요청 정보: " + pageRequestDTO);
        model.addAttribute("result", service.getList(pageRequestDTO));
    }

    // 등록화면을 보여주는 메소드 Get방식
    @GetMapping("/register")
    public void register(){
        log.info("show register");
    }
    // 등록처리 후 목록페이지로 이동 Post방식
    @PostMapping("/register")
    public String registerPost(GuestbookDTO dto, RedirectAttributes redirectAttributes){
        log.info("등록처리 후 목록페이지 리다이렉트");
        Long gno = service.register(dto);
        redirectAttributes.addFlashAttribute("msg", gno);
        return "redirect:/guestbook/list";
    }
    @GetMapping({"/read", "/modify"})
    public void read(long gno, @ModelAttribute("requestDTO") PageRequestDTO requestDTO, Model model){
        log.info("gno: " + gno);
        GuestbookDTO dto = service.read(gno);
        model.addAttribute("dto", dto);
    }

    @PostMapping("/remove")
    public String remove(Long gno, RedirectAttributes redirectAttributes){
        log.info("gno: " + gno);
        service.remove(gno);
        redirectAttributes.addFlashAttribute("msg", gno);
        return "redirect:/guestbook/list";
    }

    @PostMapping("/modify")
    public String modify(GuestbookDTO dto, @ModelAttribute("requestDTO") PageRequestDTO requestDTO, RedirectAttributes redirectAttributes){
        service.modify(dto);
        redirectAttributes.addAttribute("page", requestDTO.getPage());
        redirectAttributes.addAttribute("gno", dto.getGno());
        return "redirect:/guestbook/read";
    }
}
