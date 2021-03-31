<?php
//	type 2: return multi-row result
   require '../standard/connect.php';

   $username = mysqli_real_escape_string($con, $_POST['username']);

   $sql = "SELECT DISTINCT product FROM couple WHERE username='$username'";
   $result = mysqli_query($con, $sql);

while($row = mysqli_fetch_array($result)){
   $data = $row[0];
	if ($data) { 
	 echo $data. ",";
	}
	else {
 	 echo "No products Found";
	}
}

   mysqli_close($con);
?>