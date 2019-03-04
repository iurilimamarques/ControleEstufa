<?php 
  require 'Conexao.php';
  
   	$idestufa = $_POST['id_estufa'];
   	$mes = $_POST['mes'];
    $usuario = $_POST['idusuario'];
    $ano = $_POST['ano'];
    $status = "A";

    $query = mysqli_query($conexao, "SELECT Horario_historico,Data_historico,AVG(historico_diario.Temperatura_historico) as mediaTemperatura,AVG(Umidade_historico) as mediaUmidade FROM historico_diario WHERE Id_estufa = '$idestufa' AND STATUS_SYS = '$status' AND Id_usuario = '$usuario' AND MONTH(Data_historico) = '$mes' AND YEAR(Data_historico) = '$ano' GROUP BY Data_historico,Id_estufa");

    $info_data = array();

    while ($resultado = mysqli_fetch_assoc($query)) {
        $partes = explode("-", $resultado['Data_historico']);
        $dia = $partes[2];
      
        array_push($info_data, array("dia" => $dia,
                                     "hora" => $resultado['Horario_historico'],
        	                         "media_umidade" => round($resultado['mediaUmidade'])));	
    }
    echo json_encode(array("resposta_servidor" => $info_data));
 ?>