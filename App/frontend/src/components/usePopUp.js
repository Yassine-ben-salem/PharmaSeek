import { useState, useCallback } from 'react';

/**
 * usePopUp — lightweight popup manager
 *
 * Returns:
 *   popups   – array of active popup objects
 *   popup    – object with { valid, warning, error } shorthand helpers
 *   addPopUp – raw add function  addPopUp({ type, message, duration? })
 *   remove   – remove a popup by id
 *
 * Usage:
 *   const { popups, popup, remove } = usePopUp();
 *   popup.valid('Medication saved!');
 *   popup.warning('Stock running low.');
 *   popup.error('Connection failed.');
 *
 *   // In JSX:
 *   <PopUpContainer popups={popups} onRemove={remove} />
 */
const usePopUp = () => {
  const [popups, setPopUps] = useState([]);

  const remove = useCallback((id) => {
    setPopUps((prev) => prev.filter((t) => t.id !== id));
  }, []);

  const addPopUp = useCallback(({ type = 'valid', message, duration = 4000 }) => {
    const id = `popup-${Date.now()}-${Math.random().toString(36).slice(2, 7)}`;
    setPopUps((prev) => [...prev, { id, type, message, duration }]);
    return id;
  }, []);

  const popup = {
    valid: (message, duration) => addPopUp({ type: 'valid', message, duration }),
    warning: (message, duration) => addPopUp({ type: 'warning', message, duration }),
    error: (message, duration) => addPopUp({ type: 'error', message, duration }),
  };

  return { popups, popup, addPopUp, remove };
};

export default usePopUp;
