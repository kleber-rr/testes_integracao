package com.testeintegracao.testes.controller

import com.testeintegracao.testes.model.Contato
import com.testeintegracao.testes.service.ContatoService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import javax.validation.Valid

@Controller
@RequestMapping("/agendactrl")
class AgendaController(
    @Autowired
    val service: ContatoService
) {

    @GetMapping("/")
    fun getContatos(mav: ModelAndView): ModelAndView {
        mav.addObject("contatos", service.buscarContatos())
        return mav
    }

    @GetMapping("/contato/{id}")
    fun getContato(@PathVariable("id") id: Long, mav: ModelAndView): ModelAndView {
        mav.viewName = "agendactrl/contato"
        mav.addObject("contato", service.buscarContato(id))
        return mav
    }

    @GetMapping("/remover/{id}")
    fun remover(@PathVariable("id") id: Long, redirect: RedirectAttributes): ModelAndView {
        redirect.addFlashAttribute("successMessage", "Contato removido com sucesso")
        service.remover(id)
        return ModelAndView("redirect:agendactrl/")
    }

    @GetMapping("/cadastro")
    fun cadastro(mav: ModelAndView): ModelAndView {
        mav.addObject("contato", Contato())
        return mav
    }

    @PostMapping("/inserir")
    fun inserir(@Valid contato: Contato, bind: BindingResult, mav: ModelAndView): ModelAndView {
        if(!bind.hasErrors()){
            mav.addObject("successMessage", "Contato cadastrado com sucesso")
            service.inserirOuAlterar(contato)
        }
        return mav
    }

}