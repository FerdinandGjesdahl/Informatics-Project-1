import React, { useEffect, useState } from 'react';
import { NavLink, Link } from 'react-router-dom';
import { FaBars, FaHome, FaPencilAlt, FaKey, FaDumbbell, FaRegClipboard} from 'react-icons/fa';


import elevatuLogo from '../../resources/images/ElevatuLogo.jpg';
import './Sidebar.css';
import {useAuth} from "../../context/AuthContext";
import SessionManager from "../../helpers/SessionManager";

const Sidebar = ({children}) => {

    /** 
     * States for sidebar and for windowsize
     * Originally set depending on windowsize, but uses set function to update
     * state when it is necessary.
     */
    const initialIsMobile = window.innerWidth < 768; // Adjust the breakpoint as needed
    const [isOpen, setIsOpen] = useState(!initialIsMobile);
    const [mobile, setMobile] = useState(initialIsMobile);

    const auth = useAuth()

    /**
     * Constant function to open or close sidebar menu
     * depending on state isOpen.
     * Calls setIsOpen switches value to oppsite.
     * Does only change on mobilestate!
     * @returns {void} none
     */
    const open = () => {
        if (mobile) {
            setIsOpen(!isOpen);
        } else {
            setIsOpen(true);
            
        }
    };

    /**
     * UseEffect function to update mobile state.
     * Switches to mobile state on small window sizes and not mobile on large.
     * Also opens sidebar on large window states!
     * @returns resize
     */
    useEffect(() => {

        auth.setUserSession(SessionManager.getSessionFromCookies())
        const handleResize = () => {
            if(window.innerWidth < 1064){
                    setMobile(true);
                    setIsOpen(false);
            } else {
                setMobile(false);
                setIsOpen(true);
            }
        };
    
        window.addEventListener("resize", handleResize);
        return () => {window.removeEventListener("resize", handleResize)};
    }, []);

    const navigationMenuLoggedIn = [
        {
            path:"/",
            pageName:"Home",
            icon: <FaHome/>,
        },
        {
            path: '/workout',
            pageName: 'Start workout',
            icon: <FaDumbbell />,
        },
        {
            path: '/log', // Use an absolute path
            pageName: 'Log',
            icon: <FaRegClipboard/>,
        },
        {
            path: '/createWorkout', // Use an absolute path
            pageName: 'Create Workout',
            icon: <FaPencilAlt />,
        },
        {
            path: '/createExercise', // Use an absolute path
            pageName: 'Create Exercise',
            icon: <FaPencilAlt />,
        },
    ]

    const navigationMenuLoggedOut = [
        {
            path: '/login',
            pageName: 'Login',
            icon: <FaKey />
        },
    ]

    const navigationMenu = auth.isLoggedIn ?
        navigationMenuLoggedIn : navigationMenuLoggedOut;



    /**
     * Returns Sidebar component.
     * @returns {Sidebar} Sidebar component
     */
    return(
        <div className='container' data-testid="sidebar">
            <div className='sidebar'>
                <div className='topSection' style={{width : isOpen ? "20%" : "2.5%"}}>
                    <Link to="/" className="appLogo" style={{display: isOpen ? "block" : "none"} }> 
                        <img src={elevatuLogo} alt="ElevatU" width="75px" height="75px"/>
                    </Link>
                    <div className="navLogo" style={{marginLeft: isOpen ? "25px" : "0px"}}>
                        <FaBars onClick={open}/> {/* Icon from react-icons --save */} 
                    </div>
                </div>
                <div className='navSection'>
                    {
                        navigationMenu.map((item, index) => (
                            <NavLink to={item.path} key={index} className="navLink" activeClassName="active">
                                <div className="icon">{item.icon}</div>
                                <div className="pageName" style={{display: isOpen ? "block" : "none"}}>{item.pageName}</div>
                            </NavLink>
                        ))
                    }
                    {auth.isLoggedIn ? <div onClick={() => {
                        auth.setUserSession(null);
                        SessionManager.clearSessionCookies();
                    }} class="navLink">
                        <div className="icon"><FaKey/></div>
                        <div className="pageName">Logout</div>
                    </div> : null}
                </div>
            </div>
            <main>{children}</main>
        </div>
    );
};

export default Sidebar;