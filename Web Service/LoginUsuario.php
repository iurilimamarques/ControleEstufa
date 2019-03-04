<?php 

  require 'conexao.php';

  $Email_usuario = $_POST["Email_usuario"];
  $Senha_usuario = $_POST["Senha_usuario"];


  if (!empty($Email_usuario) && !empty($Senha_usuario)) {

    $senha_criptografada = md5($Senha_usuario);

    $status_usuario = mysqli_query($conexao,"SELECT STATUS_SYS FROM usuarios WHERE Email_usuario = '$Email_usuario' && Senha_usuario =  '$senha_criptografada';");

     $dados = $status_usuario->fetch_array(MYSQLI_ASSOC);

    if ($dados["STATUS_SYS"] != "A") {

        $inexistente = array();
        array_push($inexistente, array("erro"=>'inexistente'));
        echo json_encode(array("resposta_servidor"=>$inexistente));    

    }else{
      if (mysqli_num_rows($status_usuario) > 0) {

      $nome_usuario = mysqli_query($conexao,"SELECT Id_usuario,Nome_usuario FROM usuarios WHERE Email_usuario = '$Email_usuario' && Senha_usuario =  '$senha_criptografada';");

      $infos_usuario = array();

      while ($resultado = mysqli_fetch_assoc($nome_usuario)) {
         array_push($infos_usuario,array("id_usuario"=>$resultado['Id_usuario'],"nome"=>$resultado['Nome_usuario']));
      }
        
        echo json_encode(array("resposta_servidor"=>$infos_usuario));

      }else{
        $erro_login = array();
        array_push($erro_login, array("erro"=>'erro_login'));
        echo json_encode(array("resposta_servidor"=>$erro_login));    
    }
  }

  }else{
    $info_vazias = array();
    array_push($info_vazias, array("erro"=>'info_vazias'));
    echo json_encode(array("resposta_servidor"=>$info_vazias));

  }
    
 ?>