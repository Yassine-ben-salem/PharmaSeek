import React from 'react';
import { motion } from 'framer-motion';

const LivingBackground = ({ theme = 'pharmacy' }) => {
    // Enhanced colors for better visibility against backgrounds
    // Pharmacy (Dark Blue Background): Use brighter cyans and blues
    // Client (Light Mint Background): Use darker emeralds and greens
    const colors = theme === 'pharmacy'
        ? ['#38bdf8', '#0ea5e9', '#6366f1'] // Sky Blue, Sky 500, Indigo 500
        : ['#34d399', '#10b981', '#059669']; // Emerald 400, Emerald 500, Emerald 600

    return (
        <div className="living-background" style={{
            position: 'absolute',
            top: 0,
            left: 0,
            width: '100%',
            height: '100%',
            overflow: 'hidden',
            zIndex: 0,
            pointerEvents: 'none',
        }}>
            {/* Added White Particles for "Life" */}
            {[...Array(15)].map((_, i) => (
                <motion.div
                    key={`p-${i}`}
                    style={{
                        position: 'absolute',
                        width: Math.random() * 4 + 2 + 'px',
                        height: Math.random() * 4 + 2 + 'px',
                        background: theme === 'pharmacy' ? 'rgba(255,255,255,0.3)' : 'rgba(16, 185, 129, 0.4)',
                        borderRadius: '50%',
                        top: Math.random() * 100 + '%',
                        left: Math.random() * 100 + '%',
                    }}
                    animate={{
                        y: [0, -100],
                        opacity: [0, 1, 0],
                    }}
                    transition={{
                        duration: Math.random() * 10 + 10,
                        repeat: Infinity,
                        ease: "linear",
                        delay: Math.random() * 5,
                    }}
                />
            ))}

            {/* Ambient Blobs */}
            {[...Array(6)].map((_, i) => (
                <motion.div
                    key={i}
                    style={{
                        position: 'absolute',
                        borderRadius: '50%',
                        background: colors[i % colors.length],
                        filter: 'blur(80px)', // Increased blur for softer look
                        opacity: 0.5, // Increased opacity from 0.4
                        width: Math.random() * 400 + 200, // 200-600px (Larger)
                        height: Math.random() * 400 + 200,
                    }}
                    initial={{
                        x: Math.random() * 100 - 50 + '%',
                        y: Math.random() * 100 - 50 + '%',
                        scale: 0.8,
                    }}
                    animate={{
                        x: [
                            (Math.random() - 0.5) * 100 + '%',
                            (Math.random() - 0.5) * 100 + '%',
                            (Math.random() - 0.5) * 100 + '%',
                        ],
                        y: [
                            (Math.random() - 0.5) * 100 + '%',
                            (Math.random() - 0.5) * 100 + '%',
                            (Math.random() - 0.5) * 100 + '%',
                        ],
                        scale: [0.8, 1.4, 0.8],
                        rotate: [0, 180, 360],
                    }}
                    transition={{
                        duration: 25 + Math.random() * 15, // Slower, more majestic
                        repeat: Infinity,
                        repeatType: "reverse",
                        ease: "easeInOut",
                    }}
                />
            ))}
        </div>
    );
};

export default LivingBackground;
