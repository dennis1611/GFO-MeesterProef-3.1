<?php
//	type 3: only run query
   require '../standard/connect.php';
	
   $old_username = mysqli_real_escape_string($con, $_POST['old_username']);
   $username = mysqli_real_escape_string($con, $_POST['username']);
   $password = mysqli_real_escape_string($con, $_POST['password']);
   $email = mysqli_real_escape_string($con, $_POST['email']);
 
$sql = "UPDATE accounts SET username = '$username', password = '$password', email = '$email' WHERE username = '$old_username'";

if ($con->query($sql) === TRUE) {
    echo "Account editted succesfully";
} else {
    echo "Error editing account: " . $con->error;
}
	
   mysqli_close($con);
?>