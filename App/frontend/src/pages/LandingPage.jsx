import React, { useRef } from 'react';
import { motion, useScroll, useTransform } from 'framer-motion';
import { Link } from 'react-router-dom';
import PharmacyElements from '../components/3d/PharmacyElements';
import ClientElements from '../components/3d/ClientElements';
import DataStream from '../components/animations/DataStream';
import LivingBackground from '../components/animations/LivingBackground';
import Navbar from '../components/Navbar';
import AboutSection from '../components/AboutSection';
import ContactSection from '../components/ContactSection';
import Footer from '../components/Footer';
import './LandingPage.css';

const LandingPage = () => {
    const heroRef = useRef(null);
    const { scrollYProgress } = useScroll({
        target: heroRef,
        offset: ["start start", "end start"]
    });

    // Transformations based on scroll
    const opacity = useTransform(scrollYProgress, [0, 0.8], [1, 0]);
    const scale = useTransform(scrollYProgress, [0, 0.8], [1, 0.95]);
    const y = useTransform(scrollYProgress, [0, 0.8], [0, -50]);
    const bridgeOpacity = useTransform(scrollYProgress, [0, 0.5], [1, 0]);

    return (
        <div className="landing-page-3d">
            <Navbar />

            {/* Hero Section */}
            <div className="hero-section" id="home" ref={heroRef}>
                {/* Background Particles */}
                <motion.div className="particles" style={{ opacity }}>
                    {[...Array(20)].map((_, i) => (
                        <div key={i} className="particle" style={{
                            top: `${Math.random() * 100}%`,
                            left: `${Math.random() * 100}%`,
                            animationDelay: `${Math.random() * 5}s`
                        }}></div>
                    ))}
                </motion.div>

                {/* Left Side: Pharmacy */}
                <motion.div
                    className="split-side pharmacy-side"
                    style={{ opacity, scale, y }}
                >
                    <LivingBackground theme="pharmacy" />
                    <div className="content-wrapper">
                        <motion.div
                            className="text-content"
                            initial={{ x: -50, opacity: 0 }}
                            animate={{ x: 0, opacity: 1 }}
                            transition={{ delay: 0.2 }}
                        >
                            <h2>Pharmacy OS</h2>
                            <p>Intelligence for modern pharmacies.</p>
                            <Link to="/pharmacy" className="btn-3d btn-pharmacy">
                                Access Dashboard
                            </Link>
                        </motion.div>
                        <div className="visual-content">
                            <PharmacyElements />
                        </div>
                    </div>
                </motion.div>

                {/* Separator Effect */}
                <motion.div className="split-separator" style={{ opacity }} />

                {/* Right Side: Client */}
                <motion.div
                    className="split-side client-side"
                    style={{ opacity, scale, y }}
                >
                    <LivingBackground theme="client" />
                    <div className="content-wrapper">
                        <div className="visual-content">
                            <ClientElements />
                        </div>
                        <motion.div
                            className="text-content"
                            initial={{ x: 50, opacity: 0 }}
                            animate={{ x: 0, opacity: 1 }}
                            transition={{ delay: 0.4 }}
                        >
                            <h2>Patient App</h2>
                            <p>Your health, simplified.</p>
                            <Link to="/client" className="btn-3d btn-client">
                                Open App
                            </Link>
                        </motion.div>
                    </div>
                </motion.div>

                {/* Center Bridge Overlay */}
                <motion.div className="bridge-overlay" style={{ opacity: bridgeOpacity, scale }}>
                    <div className="logo-container">
                        <motion.h1
                            initial={{ scale: 0.8, opacity: 0 }}
                            animate={{ scale: 1, opacity: 1 }}
                            transition={{ duration: 1 }}
                        >
                            PharmaSeek
                        </motion.h1>
                        <DataStream />
                    </div>
                </motion.div>
            </div>

            {/* Content Sections */}
            <AboutSection />
            <ContactSection />
            <Footer />
        </div>
    );
};

export default LandingPage;
