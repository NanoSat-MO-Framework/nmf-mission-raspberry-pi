#!/bin/bash

fncHelp()
{
   echo ""
   echo "This script allows you to create multiple instances of simulated NanoSat segments"
   echo "that provide the NanoSat MO Framework. It can be used in combination with the"
   echo "Constellation Management Tool or as a stand-alone tool."
   echo ""
   echo "Usage: $0 [-n name] [-s size] [-f file] [-i image] [-r remove]"
   echo -e "\t-n Name: Naming scheme for the simulated NanoSat segments: nmfsim-[name]-[1 ... -s]."
   echo -e "\t\t Can contain alphanumeric and - characters. Requires -s, -i arguments"
   echo ""
   echo -e "\t-s Size: Number of NanoSat segments to be created. Default is 1 when -s is required but not provided."
   echo -e "\t\t Range: 1 - 1000, Requires -n, -i arguments"
   echo ""
   echo -e "\t-i Image: NanoSat segment Docker image"
   echo ""
   echo -e "\t-r Remove: Remove NanoSat segments. Provide either a name (see -n Naming scheme) or 'all' to remove all nmfsim-* containers."
   echo ""
   echo -e "\t-f File: will read a configuration file with kepler elements from a file to create an advanced NanoSat simulation."
   echo -e "\t\t If provided, -n and -s will be ignored. Requires -i"
   echo -e "\t\t Example:"
   echo ""
   echo -e "\t\t # advanced NanoSat segment simulation configuration file"
   echo -e "\t\t # segment name; Kepler elements: A[km];E;i[deg];RAAN[deg];ARG_PER[deg];TRUE_A[deg]"
   echo -e "\t\t supervisor-01;7021.0;0.0;98.05;340.0;0.0;0.0"
   echo -e "\t\t supervisor-02;6886.0;0.0;98.05;340.0;45.0;0.0"
   echo ""

   exit 1 # Exit script after printing help
}

fncRunBasicSimulation()
{
    re="^[0-9]+$"
    if ! [[ "$size" =~ $re ]] || ( [[ $size -lt 1 ]] || [[ $size -gt 1000 ]] )
    then
        echo "size must be a number from 1 to 1000."
        fncHelp
    fi

    re="^[a-zA-Z0-9-]+$"
    if ! [[ "$name" =~ $re ]]
    then
        echo "name must be alphanumeric"
        fncHelp
    fi

    # check if container names are already in use before creating them
    for ((i = 1 ; i <= $size ; i++))
    do
        containerName="nmfsim-$name-$i"
        fncCheckContainerName $containerName
    done

    # run containers
    for ((i = 1 ; i <= $size ; i++))
    do
        containerName="nmfsim-$name-$i"
        echo $containerName
        docker run --name $containerName -h $containerName -d $image &>/dev/null

        status=$?
        if ! [ $status -eq 0 ]
        then
            echo "Error: Could not run container $containerName"
            exit 1
        fi
    done
}

fncRunAdvancedSimulation()
{

    # check if container names are already in use before creating them
    while read -r line
    do
        # check if line is comment or empty
        if ! [[ $line = \#* ]] && ! [[ -z $line ]]
        then
            IFS=';' read -r -a elements <<< "$line"
            containerName="${elements[0]}"

            fncCheckContainerName $containerName

            # check if all elements exist
            if ! [[ ${#elements[@]} -eq 7 ]]
            then
                echo "Incorrect file format"
                fncHelp
            fi

        fi
    done < $file

    # run containers
    while read -r line
    do
        # check if line is comment or empty
        if ! [[ $line = \#* ]] && ! [[ -z $line ]]
        then
            IFS=';' read -r -a elements <<< "$line"
            containerName="${elements[0]}"
            ke_a="${elements[1]}"
            ke_e="${elements[2]}"
            ke_i="${elements[3]}"
            ke_raan="${elements[4]}"
            ke_arg_per="${elements[5]}"
            ke_true_a="${elements[6]}"

            docker run \
            --env KEPLER_A=$ke_a \
            --env KEPLER_E=$ke_e \
            --env KEPLER_I=$ke_i \
            --env KEPLER_RAAN=$ke_raan \
            --env KEPLER_ARG_PER=$ke_arg_per \
            --env KEPLER_TRUE_A=$ke_true_a \
            --name $containerName \
            -h $containerName \
            -d $image &>/dev/null

            echo $containerName

        fi
    done < $file
}

fncRemoveSegments()
{
    for containerName in $(docker container ls -a --format "{{.Names}}")
    do
        if [[ $remove == "all" ]]
        then
            if [[ $containerName == nmfsim[-]* ]]
            then
                fncRemoveContainerByName $containerName
            fi
        else
            if [[ $containerName == nmfsim[-]$remove[-]* ]]
            then
                fncRemoveContainerByName $containerName
            fi
        fi
    done
}

fncRemoveContainerByName()
{
    containerName=$1

    docker stop $containerName &>/dev/null
    docker rm $containerName
}

fncCheckContainerName()
{
    containerName=$1
    docker inspect $containerName &>/dev/null

    # if docker inspect exit code = 0 then container already exists
    status=$?
    if [ $status -eq 0 ]
    then
        echo "Docker container $containerName already exists"
        exit 1
    fi
}

while getopts "n:s:f:i:r:" args
do
    case "$args" in
        n ) name="$OPTARG" ;;
        s ) size="$OPTARG" ;;
        f ) file="$OPTARG" ;;
        i ) image="$OPTARG" ;;
        r ) remove="$OPTARG" ;;
        ? ) fncHelp ;; # print help
    esac
done

if [ -z "$size" ]
then
    size=1
fi

if [ -n "$file" ] && [ -n "$image" ]
then
    fncRunAdvancedSimulation
elif [ -n "$name" ] && [ -n "$size" ] && [ -n "$image" ]
then
    fncRunBasicSimulation
elif [ -n "$remove" ]
then
    fncRemoveSegments
else
    echo "Illegal or missing argument"
    fncHelp
fi
