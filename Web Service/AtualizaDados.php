<?php 
  
  require 'Conexao.php';

  $Id_estufa = $_POST['id_estufa'];

  $resultado = mysqli_query($conexao, "SELECT Temperatura_Estufa,Umidade_Estufa FROM estufas WHERE Id_estufa = '$Id_estufa';");
  
  $estufa = array();

  while ($rows = mysqli_fetch_assoc($resultado)) {
	array_push($estufa, array("temperatura_estufa" => $rows['Temperatura_Estufa'],
                    "umidade_estufa" => $rows['Umidade_Estufa']));
  }    
    echo json_encode(array("resposta_servidor"=>$estufa));
	
?>