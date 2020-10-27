import {aweApplication} from "./../awe";

// Position service
aweApplication.factory('Position', function () {
  var Position = {
    /**
     * Return vertical position from target
     * @private
     * @param  {Object}  target   Target object
     * @param  {Object}  layer    Source layer
     * @param  {Object}  position Object position
     * @param  {Object}  margin   Margin parameters
     * @return {Integer} Vertical position from target
     */
    _getVerticalPosition: function (target, layer, position, margin) {
      // Asignamos las variables de posicion a unas locales
      var vertical = position.vertical;
      var distance = margin.vertical;
      var top = 0;

      // Calculamos la posicion vertical del tooltip
      switch (vertical) {
        case "top":
          top = target.top;
          top -= (layer.height + distance);
          break;
        case "bottom":
          top = target.top + target.height;
          top += (distance);
          break;
        case "center":
        default:
          top = target.top + Math.floor(target.height / 2) - Math.ceil(layer.height / 2);
          break;
      }
      return top;
    },
    /**
     * Return horizontal position from target
     * @private
     * @param  {Object}  target   Target object
     * @param  {Object}  layer    Source layer
     * @param  {Object}  position Object position
     * @param  {Object}  margin   Margin parameters
     * @return {Integer} Horizontal position from target
     */
    _getHorizontalPosition: function (target, layer, position, margin) {
      // Asignamos las variables de posicion a unas locales
      var horizontal = position.horizontal;
      var distance = margin.horizontal;
      var left = 0;

      // Calculamos la posicion horizontal del tooltip
      switch (horizontal) {
        case "left":
          left = target.left;
          left -= (layer.width + distance);
          break;
        case "right":
          left = target.left + target.width;
          left += (distance);
          break;
        case "center":
        default:
          left = target.left + Math.floor(target.width / 2) - Math.ceil(layer.width / 2);
          break;
      }
      return left;
    },
    /**
     * Returns an object indicating where does the object outfits from the viewport
     * @private
     * @param  {Object}  layer      Layer dimensions
     * @param  {Object}  viewport   Viewport dimensions
     * @param  {Object}  margin     Margin parameters
     * @return {Object}  Object indicating where does the object outfits from the viewport
     * (vertical:["ok", "all", "top", "bottom"], horizontal:["ok", "all", "left", "right"])
     */
    _isInsideViewport: function (layer, viewport, margin) {
      var inside = "ok";

      // Calculamos si se sale del rango vertical
      if (layer.top + layer.height >= viewport.top + margin.vertical + viewport.height &&
        layer.top - margin.vertical < viewport.top) {
        inside = "all";
      }

      if (layer.top + layer.height >= viewport.top + margin.vertical + viewport.height) {
        inside = "bottom";
      } else if (layer.top <= viewport.top - margin.vertical) {
        inside = "top";
      }

      // Calculamos si se sale del rango horizontal
      if (layer.left + layer.width >= viewport.left + margin.horizontal + viewport.width &&
        layer.left - margin.horizontal < viewport.left) {
        inside = "all";
      }

      if (layer.left + layer.width >= viewport.left + margin.horizontal + viewport.width) {
        inside = "right";
      } else if (layer.left <= viewport.left - margin.horizontal) {
        inside = "left";
      }

      return inside;
    },
    /**
     * Returns a recalculated position depending on the outfit direction
     * @private
     * @param  {Object}  _position   Object dimensions
     * @param  {Object}  _inside     Object out viewport
     * @return {Object}  Recalculated position
     */
    _readjustPosition: function (_position, _inside) {
      /* Variable definition */
      var _newPos = {};
      var _paths = {
        top: {
          "top": "bottom",
          "bottom": "top",
          "left": "center",
          "right": "center",
          "center": "bottom"
        },
        bottom: {
          "top": "bottom",
          "bottom": "top",
          "left": "center",
          "right": "center",
          "center": "top"
        },
        left: {
          "left": "right",
          "right": "left",
          "top": "center",
          "bottom": "center",
          "center": "right"
        },
        right: {
          "left": "right",
          "right": "left",
          "top": "center",
          "bottom": "center",
          "center": "left"
        },
        all: {
          "top": "center",
          "bottom": "center",
          "left": "center",
          "right": "center",
          "center": "center"
        },
        ok: {
          "top": "top",
          "bottom": "bottom",
          "left": "left",
          "right": "right",
          "center": "center"
        }
      };

      /* Readjust positions */
      _newPos.vertical = _paths[_inside][_position.vertical];
      _newPos.horizontal = _paths[_inside][_position.horizontal];

      _position.changedVertical = _position.vertical !== _newPos.vertical;
      _position.changedHorizontal = _position.horizontal !== _newPos.horizontal;
      _.merge(_position, _newPos);

      return _position;
    },
    /**
     * Return an attribute set with the final object position
     * @public
     * @param  {Object} target   Target position
     * @param  {Object} layer    Object position
     * @param  {Object} viewport Viewport position
     * @param  {Object} position Global position parameters
     * @param  {Object} margin   Global margin parameters
     * @return {Object} Final object position (top, left)
     */
    getFinalPosition: function (target, layer, viewport, position, margin) {
      /* Variable definition */
      var _inside = null;
      var _retries = 5;
      var _history = {top: false, bottom: false, left: false, right: false, ok: false, all: false};

      /* Calculate initial position */
      layer.top = Position._getVerticalPosition(target, layer, position, margin);
      layer.left = Position._getHorizontalPosition(target, layer, position, margin);

      /* Recalculate if object is outside viewport */
      _inside = Position._isInsideViewport(layer, viewport, margin);

      /* Store in history */
      _history[_inside] = true;

      var _position = {changedVertical: false, changedHorizontal: false};
      _.merge(_position, position);

      /* Launch recalculation */
      while (_inside != "ok" && _retries > 0) {
        _position = Position._readjustPosition(_position, _inside);

        // Recalculate vertical position
        if (_position.changedVertical) {
          layer.top = Position._getVerticalPosition(target, layer, _position, margin);
        }

        // Recalculate horizontal position
        if (_position.changedHorizontal) {
          layer.left = Position._getHorizontalPosition(target, layer, _position, margin);
        }

        /* Recalculate if object is outside viewport */
        _inside = Position._isInsideViewport(layer, viewport, margin);

        /* Decrement retries */
        _retries--;
      }

      /* Last calculation vertical */
      if (_position.changedVertical) {
        layer.top = Position._getVerticalPosition(target, layer, _position, margin);
      }

      /* Last calculation horizontal */
      if (_position.changedHorizontal) {
        layer.left = Position._getHorizontalPosition(target, layer, _position, margin);
      }

      return {coordinates: layer, direction: _position.vertical === "center" ? _position.horizontal : _position.vertical};
    },
    /**
     * Check widget position
     * @param {Object} element Element to reposition
     * @param {Object} target Target component
     * @param {Object} viewport Container window
     * @param {Object} properties Calculation properties
     * @public
     */
    checkPosition: function (element, target, viewport, properties) {
      // Viewpoint dimensions
      var viewportDimensions = Position.getFastDimensions(viewport);

      // Target dimensions
      var targetDimensions = Position.getOuterDimensions(target, true);

      // Element dimensions
      var elementDimensions = Position.getOuterDimensions(element, true);

      // Get final position
      return Position.getFinalPosition(targetDimensions, elementDimensions, viewportDimensions, properties.position, properties.margin);
    },
    /**
     * Move node to a position
     * @param {object}  node Node to move
     * @param {integer} left Horizontal position
     * @param {integer} top  Vertical position
     * @public
     */
    moveTo: function (node, left, top) {
      // Move layer to new position
      $(node).css({left: left + "px", top: top + "px"});
    },
    /**
     * Reposition object
     * @param {object}  node Node to move
     * @param {object} properties Horizontal position
     * @public
     */
    reposition: function (node, properties) {
      // Add dimensions
      if ("top" in properties) {
        properties.top += "px";
      }
      if ("left" in properties) {
        properties.left += "px";
      }
      if ("width" in properties) {
        properties.width += "px";
      }
      if ("height" in properties) {
        properties.height += "px";
      }

      // Move layer to new position
      $(node).css(properties);
    },
    /**
     * Get node inner dimensions
     * @param {object}  node Node to get dimensions
     * @return {object} node dimensions
     * @public
     */
    getInnerDimensions: function (node) {
      var $node = $(node);
      var offset = $node.offset();
      var dimensions = {
        top: Math.floor(offset.top + $node.scrollTop() + parseInt($node.css('padding-top'))),
        left: Math.floor(offset.left + $node.scrollLeft() + parseInt($node.css('padding-left'))),
        width: Position.getInnerWidth($node),
        height: Position.getInnerHeight($node)
      };

      return dimensions;
    },
    /**
     * Get node inner width
     * @param {object} $node Node to get dimensions
     * @return {integer} node width
     * @public
     */
    getInnerWidth: function ($node) {
      return Math.floor($node.width());
    },
    /**
     * Get node inner height
     * @param {object} $node Node to get dimensions
     * @return {integer} node height
     * @public
     */
    getInnerHeight: function ($node) {
      return Math.floor($node.height());
    },
    /**
     * Get node outer width
     * @param {object} $node Node to get dimensions
     * @param {boolean} margin Add margin width too
     * @return {integer} node width
     * @public
     */
    getOuterWidth: function ($node, margin) {
      return Math.floor($node.outerWidth(margin));
    },
    /**
     * Get node outer height
     * @param {object} $node Node to get dimensions
     * @param {boolean} margin Add margin height too
     * @return {integer} node height
     * @public
     */
    getOuterHeight: function ($node, margin) {
      return Math.floor($node.outerHeight(margin));
    },
    /**
     * Get node dimensions
     * @param {object}  node Node to get dimensions
     * @return {object} node dimensions
     * @public
     */
    getDimensions: function (node) {
      var htmlNode = node[0] ? node[0] : node;
      var $node = $(node);
      var offset = $node.offset();
      var dimensions = {
        top: Math.floor(offset.top + $node.scrollTop()),
        left: Math.floor(offset.left + $node.scrollLeft()),
        width: Math.floor(htmlNode.offsetWidth),
        height: Math.floor(htmlNode.offsetHeight)
      };

      return dimensions;
    },
    /**
     * Get outer node dimensions
     * @param {object}  node Node to get dimensions
     * @param {boolean} outer With margins
     * @return {object} node dimensions
     * @public
     */
    getOuterDimensions: function (node, outer) {
      var $node = $(node);
      var _outer = outer ? true : false;
      var offset = $node.offset();
      var dimensions = {
        top: Math.floor(offset.top + $node.scrollTop()),
        left: Math.floor(offset.left + $node.scrollLeft()),
        width: Position.getOuterWidth($node, _outer),
        height: Position.getOuterHeight($node, _outer)
      };
      return dimensions;
    },
    /**
     * Get fast dimensions
     * @param {object}  node Node to get dimensions
     * @return {object} node dimensions
     * @public
     */
    getFastDimensions: function (node) {
      var htmlNode = node[0] ? node[0] : node;
      var dimensions = {
        top: 0,
        left: 0,
        width: htmlNode.clientWidth, //htmlNode.offsetWidth,
        height: htmlNode.clientHeight//,htmlNode.offsetHeight
      };

      return dimensions;
    },
    /**
     * Get fast dimensions
     * @param {object}  node Node to get dimensions
     * @return {object} node dimensions
     * @public
     */
    getFastFullDimensions: function (node) {
      var htmlNode = node[0] ? node[0] : node;
      var dimensions = {
        top: htmlNode.offsetTop,
        left: htmlNode.offsetLeft,
        width: htmlNode.clientWidth, //htmlNode.offsetWidth,
        height: htmlNode.clientHeight//,htmlNode.offsetHeight
      };

      return dimensions;
    }
  };

  return Position;
});
