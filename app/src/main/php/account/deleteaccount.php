<?php
//	type 3: only run query
   require '../standard/connect.php';
	
   $username = mysqli_real_escape_string($con, $_POST['username']);
   $sql = "DELETE FROM accounts WHERE username='$username'";

if ($con->query($sql) === TRUE) {
    echo "Account deleted succesfully";
} else {
    echo "Error deleting account: " . $con->error;
}
	
   mysqli_close($con);
?>