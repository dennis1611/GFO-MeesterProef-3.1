<?php
//	type 2: return multi-row result
   require '../standard/connect.php';

   $product = mysqli_real_escape_string($con, $_POST['product']);

   $sql = "SELECT dname FROM files WHERE product='$product'";	
   $result = mysqli_query($con, $sql);
  
while($row = mysqli_fetch_array($result)){
   $data = $row[0];
	if ($data) { 
	 echo $data. ",";
	}

	else {
 	 echo "Failed";
	}
}

   mysqli_close($con);
?>