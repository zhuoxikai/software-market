package com.sicau.entity.dto;

public class RunProject {
    private String runId;

    private String timeNode;

    private String delayTime;

    private String startTime;

    private String overtime;

    private String score;

    private String progress;

    private String undertakeId;

    public String getRunId() {
        return runId;
    }

    public void setRunId(String runId) {
        this.runId = runId == null ? null : runId.trim();
    }

    public String getTimeNode() {
        return timeNode;
    }

    public void setTimeNode(String timeNode) {
        this.timeNode = timeNode == null ? null : timeNode.trim();
    }

    public String getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(String delayTime) {
        this.delayTime = delayTime == null ? null : delayTime.trim();
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime == null ? null : startTime.trim();
    }

    public String getOvertime() {
        return overtime;
    }

    public void setOvertime(String overtime) {
        this.overtime = overtime == null ? null : overtime.trim();
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score == null ? null : score.trim();
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress == null ? null : progress.trim();
    }

    public String getUndertakeId() {
        return undertakeId;
    }

    public void setUndertakeId(String undertakeId) {
        this.undertakeId = undertakeId;
    }
}