<?php 

   require 'conexao.php';

   $estufa = mysqli_query($conexao,"SELECT Id_estufa FROM estufas WHERE STATUS_SYS = 'A';");

   $numero = mysqli_num_rows($estufa);

    $info_estufa = array();
    array_push($info_estufa, array("num_estufas"=> $numero));
    echo json_encode(array("resposta_servidor"=>$info_estufa));
 ?>