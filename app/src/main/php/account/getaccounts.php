<?php

$con = new mysqli("localhost","deb8634n5_gfo","LWU7i0LQ","deb8634n5_gfo");
 
if ($con->connect_error) {
   die("Connection failed: " . $con->connect_error);
}

$useraccounts = array();

$sql = "SELECT username, password, email, adminflag FROM accounts ORDER BY username ASC;";

//creating an statment with the query 
$stmt = $con->prepare($sql);

//executing that statment 
$stmt->execute();
 
//binding results for that statment 
$stmt->bind_result($username, $password, $email, $adminflag);
 
//looping through all the records
while($stmt->fetch()){
	
	//pushing fetched data in an array 
	$temp = [
		'username'=>$username,
		'password'=>$password,
		'email'=>$email,
		'adminflag'=>$adminflag
	];
	
	//pushing the array inside the hero array 
	array_push($useraccounts, $temp);
}
 
//displaying the data in json format 
echo json_encode($useraccounts);

mysqli_close($con);

?>