<?php 
  require 'Conexao.php';

  date_default_timezone_set('America/Sao_Paulo');

  $idestufa = $_POST['idestufa'];
  $data = date('H:i:s');
  $tempoatual = explode(":",$data);
  $idusuario = $_POST['idusuario'];
  $dataatual = date('Y-m-d');

  $query = mysqli_query($conexao, "SELECT HOUR(Horario_alteracao) AS hora_alteracao,
                                          MINUTE(Horario_alteracao) AS minuto_alteracao, 
                                          SECOND(Horario_alteracao) AS segundo_alteracao, 
                                          HOUR(Tempo_funcionamento) AS hora_t_func, 
                                          MINUTE(Tempo_funcionamento) AS minuto_t_func, 
                                          SECOND(Tempo_funcionamento) AS segundo_t_func, 
                                          STATUS_SYS, 
                                          HOUR(Tempo_restante_pausa) AS hora_restante, 
                                          MINUTE(Tempo_restante_pausa) AS minuto_restante, 
                                          SECOND(Tempo_restante_pausa) AS segundo_restante FROM alteracoes WHERE Id_Estufa = '$idestufa' AND Data_alteracao = '$dataatual' AND Id_usuario = '$idusuario' AND STATUS_SYS != 'I'");

  $array = mysqli_fetch_array($query);
  $info_data = array();

 if ($array['STATUS_SYS'] == "A") {
  
  $hora_termino = ($array['hora_alteracao'] + $array['hora_t_func']);
  $minuto_termino = ($array['minuto_alteracao'] + $array['minuto_t_func']);
  $segundo_termino = ($array['segundo_alteracao'] + $array['segundo_t_func']);
  
  $tempo_milli = (($hora_termino - $tempoatual[0]) * 3600000);
  $tempo_milli = $tempo_milli + (($minuto_termino - $tempoatual[1]) * 60000);
  $tempo_milli = $tempo_milli + (($segundo_termino - $tempoatual[2]) * 1000);

  array_push($info_data, array("status" => $array['STATUS_SYS'], 
                               "tempo_milli" => $tempo_milli)); 

 }elseif ($array['STATUS_SYS'] == "P") {

  $tempo_milli = $array['hora_restante'] * 3600000;
  $tempo_milli = $tempo_milli + ($array['minuto_restante'] * 60000);
  $tempo_milli = $tempo_milli + ($array['segundo_restante'] * 1000); 

    array_push($info_data, array("status" => $array['STATUS_SYS'], 
                                 "tempo_milli" => $tempo_milli)); 

 }else{
    $tempo_milli = 0;
    array_push($info_data, array("status" => "inativo", 
                                 "tempo_milli" => $tempo_milli)); 
 }
  

  echo json_encode(array("resposta_servidor" => $info_data));
?>