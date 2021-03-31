<?php
//	type 1: return single-row result
   require 'standard/connect.php';

   $username = mysqli_real_escape_string($con, $_POST['username']);
   $password = mysqli_real_escape_string($con, $_POST['password']);

   $sql = "SELECT adminflag FROM accounts WHERE username='$username' and password='$password'";
   $result = mysqli_query($con, $sql);
   $row = mysqli_fetch_array($result);
   $data = $row[0];

if ($data) { 
 echo $data;
}
else { 
 echo "Login Failed"; 
}

  mysqli_close($con);
?>