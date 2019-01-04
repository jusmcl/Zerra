#version 140

in vec2 position;

out vec2 pass_TextureCoords;

uniform mat4 projectionMatrix;
uniform mat4 transformationMatrix;
uniform vec4 textureData;

void main() {
	vec4 worldPosition = transformationMatrix * vec4(position.x, position.y, -10.0, 1.0);
	gl_Position = projectionMatrix * worldPosition;
	pass_TextureCoords = vec2(position.x * textureData.z, position.y * textureData.w) + textureData.xy;
}
