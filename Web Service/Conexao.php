<?php  
  define('hostname', 'localhost');
  define('usuario', 'root');
  define('senha', '');
  define('BDnome', 'bd_estufa');

  $conexao = mysqli_connect(hostname, usuario,senha,BDnome);

?>