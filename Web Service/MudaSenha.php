<?php 

  require 'Conexao.php';

  $senha_nova = $_POST['senha_nova'];
  $id_usuario = $_POST['id_usuario'];
  
   if (!empty($senha_nova)) {
   	  $senha_nova_criptografada = md5($senha_nova);
      
      $nova_senha = mysqli_query($conexao,"UPDATE usuarios SET Senha_usuario = '$senha_nova_criptografada' WHERE Id_usuario = '$id_usuario';");

      		   if (mysqli_affected_rows($conexao) > 0) {
              $info = array();
              array_push($info, array("mensagem"=>'atualizado'));
              echo json_encode(array("resposta_servidor"=>$info));
      		   }else{
              $info = array();
              array_push($info, array("mensagem"=>'erro'));
              echo json_encode(array("resposta_servidor"=>$info));
               }
    }else{
      $info = array();
      array_push($info, array("mensagem"=>'vazio'));
      echo json_encode(array("resposta_servidor"=>$info));
    }
 ?>