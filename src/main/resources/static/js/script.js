console.log("akash mapari is here")

const toggleSidebar =()=>{
    
    if($(".sidebar").is(":visible")){

        //band karaych ahe
        $(".sidebar").css("display","none");
        $(".content").css("margin-left","0%")
    }else{
        //show karaych ahe
        $(".sidebar").css("display","block");
        $(".content").css("margin-left","20%")
    }
};