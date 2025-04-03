username=$1
password=$2
host=$3
port=$4
ssh -p $port $username:$password@$host "sh '/vol2/@development/Gateway/start.sh\"