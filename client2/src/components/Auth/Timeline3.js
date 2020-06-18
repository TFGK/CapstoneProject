import React from 'react';
import Timeline from '@material-ui/lab/Timeline';
import TimelineItem from '@material-ui/lab/TimelineItem';
import TimelineSeparator from '@material-ui/lab/TimelineSeparator';
import TimelineConnector from '@material-ui/lab/TimelineConnector';
import TimelineContent from '@material-ui/lab/TimelineContent';
import TimelineDot from '@material-ui/lab/TimelineDot';

export default function OutlinedTimeline() {
  return (
    <Timeline align="alternate">
      <TimelineItem>
        <TimelineSeparator>
          <TimelineDot variant="outlined" color="inherit" />
          <TimelineConnector />
        </TimelineSeparator>
        <TimelineContent>영진전문대학</TimelineContent>
      </TimelineItem>
      <TimelineItem>
        <TimelineSeparator>
          <TimelineDot variant="outlined" color="primary" />
          <TimelineConnector />
        </TimelineSeparator>
        <TimelineContent>석수반점</TimelineContent>
      </TimelineItem>
      <TimelineItem>
        <TimelineSeparator>
          <TimelineDot variant="outlined" color="secondary" />
          <TimelineConnector />
        </TimelineSeparator>
        <TimelineContent>이화수육개장</TimelineContent>
      </TimelineItem>
      

      <TimelineItem>
        <TimelineSeparator>
          <TimelineDot variant="outlined" color="grey" />
          <TimelineConnector />
        </TimelineSeparator>
        <TimelineContent>CU 복현대로점</TimelineContent>
      </TimelineItem>

      <TimelineItem>
        <TimelineSeparator>
          <TimelineDot variant="outlined" color="inherit" />
        </TimelineSeparator>
        <TimelineContent>복현소방서</TimelineContent>
      </TimelineItem> 
    </Timeline>
  );
}