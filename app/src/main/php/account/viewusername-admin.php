<?php
//	type 2: return multi-row result
   require '../standard/connect.php';
	
   $sql = "SELECT username FROM accounts WHERE adminflag='Y' ORDER BY username ASC";
   $result = mysqli_query($con, $sql);
  
while($row = mysqli_fetch_array($result)){
   $data = $row[0];
	if ($data) { 
	 echo $data. ",";
	}
	else {
 	 echo "No accounts found";
	}
}
	
   mysqli_close($con);
?>