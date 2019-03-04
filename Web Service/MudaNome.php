<?php 
  
  require 'conexao.php';

  $id = $_POST['id_usuario'];
  $nome = $_POST['nome_usuario'];

  $update_nome = mysqli_query($conexao,"UPDATE usuarios SET Nome_usuario = '$nome' WHERE Id_usuario = '$id'");

  if(mysqli_affected_rows($conexao) > 0){
    $info = array();
    array_push($info, array("mensagem"=>'atualizado'));
    echo json_encode(array("resposta_servidor"=>$info));
  }else{
    $info = array();
    array_push($info, array("mensagem"=>'erro'));
    echo json_encode(array("resposta_servidor"=>$info));
  }
  
  mysqli_close($conexao);
 
 ?>