<?php 
  require 'Conexao.php';

  $idestufa = $_POST['idestufa'];
  $idusuario = $_POST['idusuario'];
  $tempo_restante = $_POST['tempo_restante'];
  $status = "P";

  $sql = mysqli_query($conexao,"UPDATE alteracoes SET STATUS_SYS = '$status', Tempo_restante_pausa = '$tempo_restante' WHERE Id_Estufa = '$idestufa' AND Id_usuario = '$idusuario' AND STATUS_SYS = 'A'");

   if(mysqli_affected_rows($conexao) > 0){
    $info = array();
    array_push($info, array("mensagem"=>'pausado'));
    echo json_encode(array("resposta_servidor"=>$info));
  }else{
    $info = array();
    array_push($info, array("mensagem"=>'erro'));
    echo json_encode(array("resposta_servidor"=>$info));
  }
 ?>