<?php 
  require 'conexao.php';

  $id = $_POST['Id_usuario'];
  $STATUS_SYS = "I";

  $update_user = mysqli_query($conexao,"UPDATE usuarios SET STATUS_SYS = '$STATUS_SYS' WHERE Id_usuario = '$id'");

  $info = array();
  if(mysqli_affected_rows($conexao) > 0){
    array_push($info, array("mensagem"=>'deletado'));
    echo json_encode(array("resposta_servidor"=>$info));
  }else{
    array_push($info, array("mensagem"=>'erroEx'));
    echo json_encode(array("resposta_servidor"=>$info));
  }
  
  mysqli_close($conexao);
 
 ?>