<?php 

  require 'conexao.php';
    
  	$Nome_usuario = $_POST["Nome_usuario"];
  	$Email_usuario = $_POST["Email_usuario"];
  	$Senha_usuario = $_POST["Senha_usuario"];
    $STATUS_SYS = "A";


    if (!empty($Nome_usuario) && !empty($Email_usuario) && !empty($Senha_usuario)) {
      
      $select = ("SELECT * FROM usuarios WHERE Email_usuario = '$Email_usuario'");
      
      $teste = mysqli_query($conexao,$select);

      $x = 0;

      
      if ( $teste !=null) {
         if ($fetch = mysqli_fetch_array($teste)) {
           $x++;
         }
      }
       
      if ( $x > 0) {

        $existente = array();
        array_push($existente, array("erro"=>'existente'));
        echo json_encode(array("resposta_servidor"=>$existente));   
      
      }else{

         $senha_criptografada = md5($Senha_usuario);

         $query = "INSERT INTO usuarios(Nome_usuario,Email_usuario,Senha_usuario,STATUS_SYS) VALUES ('$Nome_usuario','$Email_usuario','$senha_criptografada','$STATUS_SYS');";

        $sucesso = array();
        array_push($sucesso, array("sucesso"=>'sucesso'));
        echo json_encode(array("resposta_servidor"=>$sucesso));  

         mysqli_query($conexao, $query) or die (mysqli_error($conexao));
         mysqli_close($conexao);
      }

    }else{
        $vazio = array();
        array_push($vazio, array("erro"=>'vazio'));
        echo json_encode(array("resposta_servidor"=>$vazio));   
    }

 ?>