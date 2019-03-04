<?php 
  require 'Conexao.php';

  $idestufa = $_POST['idestufa'];
  $idusuario = $_POST['idusuario'];
  $status = "I";

  $sql = mysqli_query($conexao,"UPDATE alteracoes SET STATUS_SYS = '$status' WHERE Id_Estufa = '$idestufa' AND Id_usuario = '$idusuario'");

   if(mysqli_affected_rows($conexao) > 0){
    $info = array();
    array_push($info, array("mensagem"=>'cancelado'));
    echo json_encode(array("resposta_servidor"=>$info));
  }else{
    $info = array();
    array_push($info, array("mensagem"=>'erro'));
    echo json_encode(array("resposta_servidor"=>$info));
  }
 ?>