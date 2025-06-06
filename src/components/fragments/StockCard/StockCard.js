import React, { useEffect, useState } from "react";
import { useSelector, useDispatch } from "react-redux";
import { makeStyles } from "@material-ui/core/styles";
import { imageUrl } from "./../../../constants";
import { Link } from "react-router-dom";
import Moment from "react-moment";
import NumberFormat from "react-number-format";
import Button from "@material-ui/core/Button";
import Dialog from "@material-ui/core/Dialog";
import DialogActions from "@material-ui/core/DialogActions";
import DialogContent from "@material-ui/core/DialogContent";
import DialogContentText from "@material-ui/core/DialogContentText";
import Card from "@material-ui/core/Card";
import { Typography, Grid, CardContent } from "@material-ui/core";

const StockCard = props => {
  return (
    <Card>
      <Grid container style={{ minHeight: 70 }}>
        <Grid item style={{ flexGrow: 1, padding: 24 }}>
          <Typography variant="h4" color="textPrimary">
            {props.title}
          </Typography>
          <Typography variant="h5" color="textSecondary">
            {props.subtitle}
          </Typography>
        </Grid>

        <Grid
          item
          style={{
            backgroundColor: props.color,
            color: "white",
            display: "flex",
            alignItems: "center",
            justifyContent: "center",
            minWidth: 70
          }}
        >
          {React.createElement(props.icon, { fontSize: "large" })}
        </Grid>
      </Grid>
    </Card>
  );
};

export default StockCard;
