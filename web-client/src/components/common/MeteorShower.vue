<template>
    <div class="meteor-shower">
        <div class="moon"></div>
        <div class="star"></div>
        <div v-for="i of 25" :key="`meteor-${i}`" :class="`meteor-${i}`"></div>
    </div>
</template>

<script>
    export default {
        name: "MeteorShower"
    }
</script>

<style lang="scss" scoped>
    $n: 25;
    $s: 500;

    .meteor-shower {
        height: 100%;
        width: 100%;
        overflow: hidden;
        position: relative;
        background-image: radial-gradient(ellipse at top , #080e21 0%,  #1b2735 95%);
    }
    $shadow: ();

    @for $i from 1 through $s {
        $x: random(1920);
        $y: random(1000);
        $shadow: $shadow, ($x+px $y+px #FFF) !global;
    }

    .star {
        width: 1px;
        height: 1px;
        background: transparent;
        box-shadow: $shadow;
    }

    .moon {
        width: 80px;
        height: 80px;
        border-radius: 50%;
        background-image: linear-gradient(to left, #FFF, rgba(255,255,255,0));
        box-shadow: 2px 0 3px #FFF;
        transform: rotate(45deg);
        overflow: hidden;
        position: absolute;
        top: 10%;
        right: 10%;
        z-index: 1;
        animation: showHide 10s infinite;
    }

    @for $i from 1 through $n {
        $v: random(90) + 9; // left
        $h: random(250) + 50; // top
        $d: random(150)/10 + 3; // delay
        .meteor-#{$i} {
            position: absolute;
            top: $h + px;
            left: $v*1%;
            width: 300px;
            height: 1px;
            transform: rotate(-45deg);
            background-image: linear-gradient(to right, #FFF, rgba(255,255,255,0));
            animation: meteor $d+s linear  infinite;
            z-index: 2;
            &:before {
                content: "";
                position: absolute;
                width: 4px;
                height: 5px;
                border-radius: 50%;
                margin-top: -2px;
                background: rgba(#FFF,.7);
                box-shadow: 0 0 15px 3px #FFF;
            }

        }
    }

    @keyframes meteor {
        0% {
            opacity: 1;
            margin-top: -300px;
            margin-right: -300px;
        }
        12% {
            opacity: 0;
        }
        15% {
            margin-top: 300px;
            margin-left: -600px;
            opacity: 0;
        }
        100% {
            opacity: 0;
        }
    }

    @keyframes showHide {
        0%, 100% {
            opacity: 1;
        }
        70% {
            opacity: 0;
        }
    }
</style>