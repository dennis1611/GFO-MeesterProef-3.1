<?php
//	type 2: return multi-row result
   require '../standard/connect.php';

   $sql = "SELECT DISTINCT pname FROM products";	
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