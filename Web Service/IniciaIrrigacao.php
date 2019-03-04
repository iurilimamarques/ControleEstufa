<?php  

   require 'Conexao.php';

   date_default_timezone_set('America/Sao_Paulo');

   $comentario = $_POST['comentario'];
   $tempo_funcionamento = $_POST['tempo_funcionamento'];
   $id_usuario = $_POST['id_usuario'];
   $id_estufa = $_POST['id_estufa'];
   $data = date('Y-m-d');
   $horario = date('H:i:s');
   $status = "A";

   $alteracao = mysqli_query($conexao,"INSERT INTO alteracoes(Horario_alteracao,Data_alteracao,Tempo_funcionamento,Comentario_alteracao,STATUS_SYS,Id_usuario,Id_estufa) VALUES ('$horario','$data','$tempo_funcionamento','$comentario','$status','$id_usuario','$id_estufa')");

  if(mysqli_affected_rows($conexao) > 0){
    $info = array();
    array_push($info, array("mensagem"=>'iniciado'));
    echo json_encode(array("resposta_servidor"=>$info));
  }else{
    $info = array();
    array_push($info, array("mensagem"=>'erro'));
    echo json_encode(array("resposta_servidor"=>$info));
  }
 ?>