#version 140

in vec2 position;
in vec2 textureCoords;

out vec2 pass_TextureCoords;

uniform mat4 projectionMatrix;
uniform mat4 transformationMatrix;
uniform mat4 viewMatrix;

void main() {
	gl_Position = projectionMatrix * viewMatrix * transformationMatrix * vec4(position.x, position.y, -10.0, 1.0);
	pass_TextureCoords = textureCoords;
}
