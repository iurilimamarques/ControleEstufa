<?php 

  require 'conexao.php';

  $id_usuario = $_POST["id_usuario"];
  $tempo = $_POST["tempo"];
  $id_estufa = $_POST["id_estufa"];

  $select_alteracao = mysqli_query($conexao,"SELECT Horario_alteracao,Data_alteracao,Tempo_funcionamento,Comentario_alteracao,Id_Estufa,STATUS_SYS FROM alteracoes WHERE Id_usuario = '$id_usuario' AND Id_estufa = '$id_estufa' AND Data_alteracao BETWEEN date_add(CURRENT_DATE(), INTERVAL -'$tempo' DAY) AND CURRENT_DATE() ORDER BY Data_alteracao DESC,Horario_alteracao DESC;");

      $infos_alteracao = array();

      while ($resultado = mysqli_fetch_assoc($select_alteracao)) {
        array_push($infos_alteracao,array("horario"=>$resultado['Horario_alteracao'],
                                          "data"=>$resultado['Data_alteracao'],
                                          "comentario"=>$resultado['Comentario_alteracao'],
                                          "funcionamento"=>$resultado['Tempo_funcionamento'],
                                          "idestufa"=>$resultado['Id_Estufa'],
                                          "status"=>$resultado['STATUS_SYS']));
      }

      echo json_encode(array("resposta_servidor"=>$infos_alteracao));

      if ($infos_alteracao == null) {
      	 $json['inexistente'] = 'nulo';
        echo json_encode($json);
      }
    

 ?>