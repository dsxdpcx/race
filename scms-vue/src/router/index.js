import Vue from 'vue'
import VueRouter from 'vue-router'
import Login from '../components/Login'
import Home from '../components/Home'
import Welcome from "../components/Welcome";
import Page401 from "../components/401"
import SeasonList from "../components/admin/SeasonList";
import TeamList from "../components/admin/TeamList";
import UserList from "../components/admin/UserList";
import ItemList from "../components/admin/ItemList";
import AthleteList from "../components/admin/AthleteList";
import CaipanList from "../components/admin/CaipanList";
import ScoreList from "../components/admin/ScoreList";
import RecordList from "../components/admin/RecordList";
import PersonRanking from "../components/admin/PersonRanking";
import TeamRanking from "../components/admin/TeamRanking";
import SyslogList from "../components/admin/SyslogList";
import SystemReset from "../components/admin/SystemReset";
import SignItem from "../components/athlete/SignItem";
import MyItem from "../components/athlete/MyItem";
import AthleteScoreList from "../components/admin/AthleteScoreList";
import EquipmentList from "@/components/admin/EquipmentList";
import ShenheList from "@/components/admin/ShenheList";
import BorrowList from "@/components/admin/Borrow";
import MyBorrow from "@/components/fuzeren/MyBorrow";
import Borrow from "@/components/fuzeren/BorrowList";
import MessageList from "@/components/admin/MessageList";
import Index from "@/components/personal/index";
import My_collect from "@/components/personal/my_collect";
import User_info from "@/components/personal/user_info";
import racefootball from "@/components/admin/racefootball";
import Mycheer from "@/components/athlete/Mycheer";
import CheerList from "@/components/admin/CheerList";
import Schedule from "@/components/admin/Schedule";

Vue.use(VueRouter);

const routes = [{
    path: "/",
    redirect: "/login",
},
    {
        path: "/personal",
        component: Index
    },
    {
        path: "/personal/userinfo",
        component: User_info
    },
    {
        path: "/personal/mycollect",
        component: My_collect
    },
    {
        path: "/login",
        component: Login
    },
    {
        path: "/home",
        component: Home,
        redirect: "welcome",
        children: [{
            path: "/welcome",
            component: Welcome
        },
            {
                path: "/401",
                component: Page401
            },
            {
                path: "/season/seasonlist",
                component: SeasonList
            },
            {
                path: "/team/teamlist",
                component: TeamList
            },
            {
                path: "/user/userlist",
                component: UserList
            },
            {
                path: "/item/itemlist",
                component: ItemList
            },
            {
                path: "/athlete/athletelist",
                component: AthleteList
            },
            {
                path: "/athlete/caipanlist",
                component: CaipanList
            },
            {
                path: "/equipment/equipmentlist",
                component: EquipmentList
            },
            {
                path: "/equipment/borrowlist",
                component: BorrowList
            },
            {
                path: "/equipment/borrow",
                component: Borrow
            },
            {
                path: "/equipment/myborrow",
                component: MyBorrow
            },
            {
                path: "/athlete/shenhelist",
                component: ShenheList
            },
            {
                path: "/score/scorelist",
                component: ScoreList
            },
            {
                path: "/score/athletescorelist",
                component: AthleteScoreList
            },
            {
                path: "/score/schedule",
                component: Schedule
            },
            {
                path: "/ranking/personRanking",
                component: PersonRanking
            },
            {
                path: "/ranking/teamRanking",
                component: TeamRanking
            },

            {
                path: "/record/recordlist",
                component: RecordList
            },

            {
                path: "/syslog/sysloglist",
                component: SyslogList
            },
            {
                path: "/syslog/systemreset",
                component: SystemReset
            },

            {
                path: "/athleteItem/signitem",
                component: SignItem
            },
            {
                path: "/athleteItem/myitem",
                component: MyItem
            },{
                path: "/message/mymessage",
                component: MessageList
            },
            {
                path: "/race/racefootball",
                component: racefootball
            },
            {
                path: "/radio/mycheer",
                component: Mycheer
            },
            {
                path: "/radio/cheerlist",
                component: CheerList
            },
        ]
    }
];

const router = new VueRouter({
    mode: 'history',
    routes
});

export default router
