package exception;

// Criada para permitir um polimorfismo ao transportar diferentes enums
public interface GenericExceptionEnum {

  // Métodos de assinatura
  public String exibirMensagem();

  public String nomeCampoErro();
}
