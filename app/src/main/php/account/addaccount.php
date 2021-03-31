<?php
//	type 3: only run query
   require '../standard/connect.php';

   $username = mysqli_real_escape_string($con, $_POST['usernamec']);
   $password = mysqli_real_escape_string($con, $_POST['passwordc']);
   $email = mysqli_real_escape_string($con, $_POST['emailc']);
   $adminflag = mysqli_real_escape_string($con, $_POST['adminflagc']);

$sql = "INSERT INTO accounts
VALUES ('$username', '$password', '$email', '$adminflag')";

if ($con->query($sql) === TRUE) {
    echo "Succes";
} 

 else { echo "Failed"; }

  mysqli_close($con);
?>   