package br.ufrn.PDSgrupo5.exception;

import org.springframework.validation.BindingResult;

public class ValidacaoException extends Exception{
    private static final long serialVersionUID = 1L;

    private BindingResult bindingResult;

    public ValidacaoException(BindingResult br){
        this.bindingResult = br;
    }

    public BindingResult getBindingResult() {
        return bindingResult;
    }

    public void setBindingResult(BindingResult bindingResult) {
        this.bindingResult = bindingResult;
    }
}
