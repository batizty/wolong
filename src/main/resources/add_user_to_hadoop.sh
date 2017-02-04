#!/bin/sh

#### Common Functions ####
function checkHDFS() {
    target_file="/user/$user_name"
    runCommands "hadoop fs -test -e ${target_file}"
    result=$?
    if [ $result -eq 0 ]; then
        return 1
    else
        return 0
    fi
}

function init() {
    target_file="/user/$user_name"
    printInfo "init $target_file for user $user_name"
    runCommands "$hadoop_user_prefix hadoop fs -mkdir $target_file"
    ret=$?

    if [ $ret -ne 0 ]; then
        printError "init $target_file for user $user_name failed"
        return $ret
    fi

    printInfo "init $target_file ownership for user $user_name:$group_name"
    runCommands "$hadoop_user_prefix hadoop fs -chown -R $user_name:$group_name $target_file"
    ret=$?

    if [ $ret -ne 0 ]; then
        printError "init $target_file ownership for user $user_name:$group_name"
        return $ret
    fi
    return 0
}

function runCommands() {
    cmd=$1
    failed_times=0
    while [ $failed_times -lt 3 ]; do
        echo -e "run command : $cmd"
        `$cmd`
        if [ $? -eq 0 ]; then
            return 0
        else
            failed_times=`expr $failed_times + 1`
        fi
    done
    return 1
}

## Global Functions
function printInfo {
    date=`date -u`;
    echo "$date [INFO] : $1"
}

function printError {
    date=`date -u`
    echo "$date [ERROR] : $1"
}

printInfo "add hadoop user for $user_name:$group_name"

#### Setting Environment ####
export HADOOP_HOME="/usr/local/hadoop"
export HADOOP_CONF_DIR="$HADOOP_HOME/etc/hadoop"
source "${HADOOP_CONF_DIR}/hadoop-env.sh"

#### Generate by Program ####
user_array=()
group_array=()

for ((i = 0; i < ${#user_array[@]}; i++)); do
    user_name=${user_array[$i]}
    group_name=${group_array[$i]}
    echo "User $user_name:$group_name"


    #### check hadoop HDFS have this user and group ####
    printInfo "check user $user_name exists or not"
    checkHDFS
    RET=$?
    #### if not exists, init files for this user and group ####
    if [ $RET -eq 0 ]; then
        printInfo "user $user_name is not in HDFS and init for $user_name"
        init
        RET=$?

        #### add Linux Users
        id $user_name >& /dev/null
        if [ $? -eq 0 ]; then
            password=`echo "${user_name}_${group}" | md5sum | head -c 10`
            echo "$password" | sudo passwd --stdin "$user_name"
            MAILLIST="fanqi,tuoyu,chengzhao1,$user_name"
            SUBJECT="New Computing Engine Account"
            CONTENT="User : $user_name Group : $group_name Your account : $user_name Passwd: $password"
            curl -d "sv=微博平台&service=data_system&object=process&subject=$SUBJECT&content=$CONTENT&mailto=$MAILLIST&msgto=$MAILLIST" "http://suic.intra.sina.com.cn:9090/alarm/sendByTeamName"
            printInfo "init User $user_name OK"
        else
            printError "Coult not find User $user_name, please rt firstly"
        fi
    else
        printError "user $user_name is already in system"
    fi


    #### mkdir /data0/$user_name
    # TODO
done
