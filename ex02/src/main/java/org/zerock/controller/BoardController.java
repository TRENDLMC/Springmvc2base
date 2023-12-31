package org.zerock.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.domain.BoardVO;
import org.zerock.domain.Criteria;
import org.zerock.domain.PageDTO;
import org.zerock.service.BoardService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

@Log4j
@Controller
@RequestMapping("/board/*")
@AllArgsConstructor
public class BoardController {
	
	
	private BoardService service;
	//@RequestParam("page") int page
	@GetMapping("/list")
	public void list(Criteria cri ,Model model) {
		log.info("list");
		int total=service.getTotal(cri);
		model.addAttribute("list", service.getList(cri));
		model.addAttribute("pageMaker",new PageDTO(cri, total));
	}

	@PostMapping("/register")
	public String register(BoardVO board,RedirectAttributes rttr) {
		//
		log.info("register:"+board);
		service.register(board);
		//�꽌鍮꾩뒪�쓽 �젅吏��뒪�듃�뿉�꽌 dao濡� 蹂대궦�썑 db�뿉 �엯�젰�떆�궡
		rttr.addFlashAttribute("result", board.getBno());
		return "redirect:/board/list";
	}
	@GetMapping("/register")
	public void register() {
	
	}
	
	@GetMapping({"/get","/modify"})
	public void get(@RequestParam("bno") long bno,@ModelAttribute("cri") Criteria cri, Model model) {
		log.info("/get  modify");
		model.addAttribute("board",service.get(bno));
		
	}
	
	
	@PostMapping("/modify")
	public String modify(BoardVO board,RedirectAttributes rttr,@ModelAttribute("cri") Criteria cri) {
		log.info("modify:"+board);
		
		if(service.modify(board)) {
			rttr.addFlashAttribute("result","success");
		}
		
	
		
		return "redirect:/board/list"+cri.getListLink();
		
	}
	//@GetMapping("/modify")
	//public 
	
	@PostMapping("/remove")
	public String remove(@RequestParam("bno") long bno,RedirectAttributes rttr,@ModelAttribute("cri") Criteria cri){
		log.info("remove:"+bno);
		if(service.remove(bno)) {
			rttr.addFlashAttribute("result","success");
		}
		return "redirect:/board/list"+cri.getListLink();
	}
}
