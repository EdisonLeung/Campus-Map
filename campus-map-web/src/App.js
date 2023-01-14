import React, { useEffect } from 'react';

// import CampusMaps component
import CampusMap from "./components/CampusMap";

import resumeData from './resumeData';


const AppWrapper = () => {
  return (
    <CampusMap resumeData={resumeData} />
  );
};

export default AppWrapper;