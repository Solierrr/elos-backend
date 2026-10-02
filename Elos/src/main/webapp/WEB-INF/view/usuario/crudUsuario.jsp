<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>Title</title>
</head>

<style>

    th{

        background: green;
        color: white;
    }

    /* Alinha todos os elementos em uma única linha horizontal */
    .formulario-linha {
        display: flex;
        flex-wrap: wrap; /* Permite quebrar a linha se a tela for muito pequena */
        gap: 15px;       /* Cria um espaçamento igual entre os campos */
        align-items: flex-end; /* Alinha os fundos dos campos na mesma altura */
    }

    /* Organiza o rótulo acima de cada respectivo campo */
    .campo {
        display: flex;
        flex-direction: column;
    }

    /* Estilização básica para os inputs e selects ficarem padronizados */
    .campo input, .campo select {
        padding: 6px;
        margin-top: 5px;
        border: 1px solid #ccc;
        border-radius: 4px;
    }

</style>

<body>

<!-- Exibe mensagens de erro vindas do ReadUsuarioServlet, se houver -->
<c:if test="${not empty sessionScope.errosRead}">
    <div style="border:1px solid red; background:#ffe6e6; padding:10px; margin-bottom:10px;">
        <ul>
            <c:forEach var="erro" items="${sessionScope.errosRead}">
                <li>${erro.exibirMensagem()}</li>
            </c:forEach>
        </ul>
    </div>
</c:if>

<table border="3px">
    <tr>
        <th>ID</th>
        <th>Tipo de Usuário</th>
        <th>Email</th>
        <th>Nome</th>
        <th>Raio de procura em km</th>
        <th>Ações</th>

    </tr>

    <!-- Read -->
    <c:choose>
        <c:when test="${not empty usuariosRead}">
            <c:forEach var="usuario" items="${usuariosRead}">
                <tr>
                    <td>${usuario.id}</td>
                    <td>${usuario.tipoUsuario.tipoDoUsuario}</td>
                    <td>${usuario.email}</td>
                    <td>${usuario.nome}</td>
                    <td>${usuario.raioProcuraKm}</td>
                    <td>
                        <form action="${pageContext.request.contextPath}/crudUsuario-delete" method="post" style="margin: 0;">

                        <input type="hidden" name="idDelete" value="${usuario.id}">

                        <!-- Seu botão vermelho X -->
                        <button type="submit" style="background-color: red; color: black; font-weight: bold;">
                            X
                        </button>

                        </form>

                        <form action="${pageContext.request.contextPath}/crudUsuario-update" method="get" style="margin: 0;">

                            <input type="hidden" name="idUpdate" value="${usuario.id}">

                            <!-- Seu botão vermelho X -->
                            <button id="editar" type="submit" style="background-color: red; color: black; font-weight: bold;">
                                Editar
                            </button>

                        </form>

                    </td>

                </tr>
            </c:forEach>
        </c:when>

        <c:otherwise>
            <tr>
                <td colspan="6" style="text-align:center;">Nenhum usuário encontrado</td>
            </tr>
        </c:otherwise>
    </c:choose>
</table>

<!-- pop up editar-->
<dialog id="dialog1">
    <form action="${pageContext.request.contextPath}/crudUsuario-update" method="post">

        <input type="hidden" id="idUpdate" name="idUpdate" value="${sessionScope.usuarioUpdate.id}">
        <input type="hidden" id="tipoUsuarioUpdate" name="tipoUsuarioUpdate" value="${sessionScope.usuarioUpdate.tipoUsuario.tipoDoUsuario}">

        <table border="3px">
            <tr>
                <th><label for="idUpdate">ID</label></th>
                <th><label for="tipoUsuarioUpdate">Tipo do usuário</label></th>
                <th><label for="emailUpdate">Email</label></th>
                <th><label for="senhaUpdate">Senha</label></th>
                <th><label for="nomeUpdate">Nome</label></th>
                <th><label for="raioProcuraKmUpdate">Raio de procura em km</label></th>
            </tr>
            <tr>
                <td>${sessionScope.usuarioUpdate.id}</td>
                <td>${sessionScope.usuarioUpdate.tipoUsuario.tipoDoUsuario}</td>
                <td><input type="text" id="emailUpdate" name="emailUpdate" value="${sessionScope.usuarioUpdate.email}"required></td>
                <td><input type="text" id="senhaUpdate" name="senhaUpdate"></td>
                <td><input type="text" id="nomeUpdate" name="nomeUpdate" value="${sessionScope.usuarioUpdate.nome}" required></td>
                <td><input type="text" id="raioProcuraKmUpdate" name="raioProcuraKmUpdate" value="${sessionScope.usuarioUpdate.raioProcuraKm}"required></td>
            </tr>
        </table>

        <div style="margin-top: 10px;">
            <button type="submit">Enviar</button>
            <button type="button" id="fechar">Sair</button>
        </div>
    </form>
</dialog>


<!-- read caixas-->
<div class="formulario-linha">
    <form action="${pageContext.request.contextPath}/crudUsuario" method="post">

        <!-- Campo 1: Seleção (Existente) -->
        <div class="campo">
            <label for="clausulaWhereNome">clausulaWhereNome:</label>
            <select id="clausulaWhereNome" name="clausulaWhereNome">
                <option value="nenhuma">Nenhuma</option>
                <option value="id">ID</option>
                <option value="nome">Nome</option>
                <option value="email">Email</option>
                <option value="tipo_usuario">Tipo usuário</option>
                <option value="raio_procura_km">Raio de procura em km</option>
            </select>
        </div>

        <!-- Campo 2: Texto (Novo) -->
        <div class="campo">
            <label for="clausulaWhereValor">clausulaWhereValor:</label>
            <input type="text" id="clausulaWhereValor" name="clausulaWhereValor" placeholder="Digite seu nome">
        </div>

        <div class="campo">
            <label for="clausulaWhereValor2">clausulaWhereValor2:</label>
            <input type="text" id="clausulaWhereValor2" name="clausulaWhereValor2">
        </div>

        <!-- Campo 3: Seleção (Novo) -->
        <div class="campo">
            <label for="orderBy">orderBy:</label>
            <select id="orderBy" name="orderBy">
                <option value="nenhuma">Nenhuma</option>
                <option value="id">ID</option>
                <option value="nome">Nome</option>
                <option value="email">Email</option>
                <option value="tipo_usuario">Tipo usuário</option>
                <option value="raio_procura_km">Raio de procura em km</option>
            </select>
        </div>

        <div class="campo">
            <label for="ordenacao">Ordenação:</label>
            <select id="ordenacao" name="ordenacao">
                <option value="asc">Crescente</option>
                <option value="desc">Decrescente</option>
            </select>
        </div>


        <div style="margin-top: 10px;">
            <button type="submit">Enviar</button>
        </div>
    </form>
</div>




<br>
<br>

<button id="butao">Inserir</button>




<!-- pop up inserir -->
<dialog id="dialog">
    <form action="${pageContext.request.contextPath}/crudUsuario-insert" method="post">
        <table border="3px">
            <tr>
                <th><label for="tipoUsuarioInsert">Tipo do usuário</label></th>
                <th><label for="email">Email</label></th>
                <th><label for="senha">Senha</label></th>
                <th><label for="nome">Nome</label></th>
                <th><label for="raioProcuraKm">Raio de procura em km</label></th>
            </tr>
            <tr>
                <td>
                    <select id="tipoUsuarioInsert" name="tipoUsuarioInsert" >
                        <option value="FORNECEDOR" ${sessionScope.tipoUsuario.tipoDoUsuario == "FORNECEDOR" ? "selected" : ""}>Fornecedor</option>
                        <option value="EMPRESA_DEMANDANTE" ${sessionScope.tipoUsuario.tipoDoUsuario == "EMPRESA_DEMANDANTE" ? "selected" : ""}>Empresa demandante</option>
                        <option value="PROFISSIONAL" ${sessionScope.tipoUsuario.tipoDoUsuario == "PROFISSIONAL" ? "selected" : ""}>Profissional</option>
                    </select>
                </td>
                <td><input type="text" id="emailInsert" name="emailInsert" required value="${sessionScope.emailInsert}"></td>
                <td><input type="text" id="senhaInsert" name="senhaInsert" required ></td>
                <td><input type="text" id="nomeInsert" name="nomeInsert" required value="${sessionScope.nomeInsert}"></td>
                <td><input type="text" id="raioProcuraKmInsert" name="raioProcuraKmInsert" value="${sessionScope.raioProcuraKmInsert}"></td>
            </tr>
        </table>

        <div style="margin-top: 10px;">
            <button type="submit">Enviar</button>
            <button type="button" id="close">Sair</button>
        </div>
    </form>
</dialog>

<form id="casoSair" action="${pageContext.request.contextPath}/crudUsuario" method="post">

    <input type="hidden" id="sairPressionado" name="sairPressionado">

</form>

<script>

    const butao = document.getElementById("butao");
    const modal = document.getElementById("dialog");
    const butao1 = document.getElementById("close");

    const abrirInsert = ${not empty sessionScope.abrirInsert ? sessionScope.abrirInsert : false};

    const abrirUpdate = ${not empty sessionScope.abrirUpdate ? sessionScope.abrirUpdate : false};

    const modal1 = document.getElementById("dialog1");

    console.log(abrirUpdate);
    console.log(abrirInsert);

    const butao4 = document.getElementById("fechar");


    butao4.onclick = function(){
        document.getElementById("sairPressionado").value = "true";
        document.getElementById("casoSair").submit();
    }

    window.addEventListener('load', function (){

        if(abrirInsert === true){

            modal.showModal();

        }

        if(abrirUpdate === true){

            modal1.showModal();

        }

    })

    // Abre o modal manualmente
    butao.onclick = function(){
        modal.showModal();
    }



    // Fecha o modal manualmente
    butao1.onclick = function(){
        document.getElementById("sairPressionado").value = "true";
        document.getElementById("casoSair").submit();

    }

</script>

</body>
</html>
